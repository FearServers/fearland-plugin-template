plugins {
    java
}

group = "br.fearland"
version = property("plugin.version").toString()

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.xenondevs.xyz/releases")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of((findProperty("java.version") as String).toInt()))
    }
}

dependencies {
    // FearLand core is provided at runtime by the fearland-api jar and must never be bundled.
    compileOnly("br.fearland:core:${property("fearland.version")}")
    compileOnly("br.fearland:paper:${property("fearland.version")}")
    compileOnly("io.papermc.paper:paper-api:${property("paper.api.version")}")
    compileOnly("dev.jorel:commandapi-paper-core:${property("commandapi.version")}")
    compileOnly("org.jetbrains:annotations:26.0.2")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.processResources {
    val props = mapOf("version" to project.version.toString())
    inputs.properties(props)
    filesMatching("plugin.yml") { expand(props) }
}

tasks.jar {
    archiveBaseName.set(rootProject.name)
    archiveVersion.set(project.version.toString())
    archiveClassifier.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn(configurations.runtimeClasspath)
    from({ configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) } })
    exclude("br/fearland/core/**", "br/fearland/runtime/**", "br/fearland/libs/**")
}

// Deploy button (IDE Gradle panel > deployment): runs `fl deploy --force-send` from this directory. It builds the working tree here (fl runs
// the Gradle build itself, so this task has no build dependency), sends it over ssh and installs it on staging: no commit, push or GitHub
// Actions. Not part of build/check. `-PnoRestart` installs without restarting the servers.
tasks.register<Exec>("deployStaging") {
    group = "deployment"
    description = "Builds the working tree and installs it on staging over ssh (runs `fl deploy --force-send`)"
    workingDir = projectDir
    val flArgs = listOf("deploy", "--force-send") + if (project.hasProperty("noRestart")) listOf("--no-restart") else listOf()
    commandLine(
        if (System.getProperty("os.name").startsWith("Windows")) listOf("cmd", "/C", "..\\..\\tools\\fl.cmd") + flArgs
        else listOf(file("../../tools/fl").path) + flArgs
    )
}
