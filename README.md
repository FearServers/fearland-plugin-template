# fearland-plugin-template

Starting point for a new FearLand Paper plugin. Click **Use this template**, name the repository `fearland-<name>`
under the `FearServers` organization, then:

1. Rename the module: `rootProject.name` in `settings.gradle.kts`, `group`/package `br.fearland.example`,
   `plugin.yml` (`name`, `main`, `description`), the artifact `id`/`prefix` in `deploy-targets.json`.
2. Set the destination servers in `deploy-targets.json` (`velocity`, `login`, `lobby`, `smp`, `eventos`, `builds`).
3. Create the `dev` branch from `main`; protected GitHub settings (`FEARLAND_CHECKOUT_TOKEN`,
   `FEARLAND_PTERODACTYL_CONFIG_*`, `FEARLAND_PTERODACTYL_KEY_*`) are added by the repository owner.
4. In the private FearLand workspace run `tools/fl ci sync <repo>` to pin the CI caller to the current pipeline
   and `tools/fl claude sync <repo>` to refresh the shared Claude files.

## What is here

| Path | Purpose |
|---|---|
| `build.gradle.kts`, `gradle.properties`, `gradle/` | Gradle build, approved dependency versions, `gradlew` |
| `src/main/java`, `src/main/resources/plugin.yml` | minimal `JavaPlugin` |
| `deploy-targets.json` | the artifact id and the servers it is deployed to |
| `.github/workflows/fearland.yml` | CI caller for the shared FearLand pipeline |
| `CLAUDE.md`, `AGENTS.md`, `.claude/` | project instructions for Claude Code and other agents |

## Branches and CI

- feature branch or pull request: build and test only.
- push to `dev`: build/test, artifact `<artifact-id>-dev-<short-commit>.jar`, deploy to staging.
- pull request `dev` to `main`: build and test only.
- push to `main`: build/test, artifact `<artifact-id>-main-<short-commit>.jar`, deploy to production.

The build compiles against the private `br.fearland:core`/`paper` API (`compileOnly`); the shared pipeline installs it
from `fearland-api`. A standalone checkout therefore needs that API in the local Maven repository.
