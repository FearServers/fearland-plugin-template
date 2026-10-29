import groovy.json.JsonSlurper

// Local deploys belong to `fl` (artifact store, desired state for `fl drift`, backup, restart, health, rollback and the
// shared .deploy.lock). This script only builds the jars and hands them over: `fl plugin deploy --use-built` never starts Gradle.

data class DeployArtifact(
    val id: String,
    val platform: String,
    val task: String,
    val prefix: String,
    val destinations: List<String>,
)

fun loadDeployTargets(jsonFile: File): List<DeployArtifact> {
    val raw = JsonSlurper().parse(jsonFile) as Map<*, *>
    val artifacts = raw["artifacts"] as? List<*>
        ?: throw GradleException("${jsonFile.name}: missing artifacts[]")
    return artifacts.map { item ->
        val map = item as Map<*, *>
        val destinations = (map["destinations"] as List<*>).map { it.toString() }
        DeployArtifact(
            id = map["id"].toString(),
            platform = map["platform"].toString(),
            task = map["task"].toString(),
            prefix = map["prefix"].toString(),
            destinations = destinations,
        )
    }
}

// tools/fl of the FearLand workspace that contains this project (FL overrides the location)
fun findFl(start: File): File {
    System.getenv("FL")?.trim()?.takeIf { it.isNotEmpty() }?.let { return File(it) }
    var current: File? = start.canonicalFile
    while (current != null) {
        if (File(current, "tools/workspace.toml").isFile && File(current, "AGENTS.md").isFile) {
            return File(current, "tools/fl")
        }
        current = current.parentFile
    }
    throw GradleException("tools/fl not found above ${start}: build and deploy with `fl plugin deploy`, or set FL to the fl launcher")
}

val deployJson = listOf(file("deploy-targets.json"), rootProject.file("deploy-targets.json"))
    .firstOrNull { it.isFile }
    ?: throw GradleException("deploy-targets.json not found in ${projectDir} or ${rootProject.projectDir}")

val deployArtifacts = loadDeployTargets(deployJson)

tasks.register<Exec>("deployTestServer") {
    group = "deployment"
    description = "Deploys the jars built here to the local test-server through `fl plugin deploy`. Does not run from build/check."
    deployArtifacts.forEach { artifact ->
        dependsOn(if (artifact.task.startsWith(":")) artifact.task else tasks.named(artifact.task))
    }
    workingDir = rootProject.projectDir
    ((findProperty("fearlandTestServerDir") as String?)?.trim()?.takeIf { it.isNotEmpty() })?.let {
        environment("FEARLAND_TEST_SERVER_DIR", file(it).canonicalPath)
    }
    doFirst {
        commandLine(findFl(rootProject.projectDir).absolutePath, "plugin", "deploy", rootProject.projectDir.absolutePath, "--env", "local", "--use-built")
    }
}
