# FearLand plugin template

Starting point for a new first-party FearLand Paper plugin. It contains only the Gradle build, a minimal plugin and the
deployment-target contract. CI and agent configuration are **not** part of the template: the FearLand workspace tool
(`fl`) generates them.

## New plugin

1. **Use this template** and create `FearServers/fearland-<name>`; clone it into `plugins/` of the FearLand workspace.
2. Rename the example: `rootProject.name` (`settings.gradle.kts`), the package `br.fearland.example` and main class,
   `src/main/resources/plugin.yml`, and `id`/`prefix` in `deploy-targets.json`.
3. Set `destinations` in `deploy-targets.json` to servers that exist in `fearland-server/servers/`.
4. Register the repository in `tools/workspace.toml` of the workspace, and create `dev` from `main`.
5. From the workspace: `tools/fl ci sync fearland-<name>` (installs the generated PR-only CI caller) and
   `tools/fl claude sync fearland-<name>` (installs the shared agent setup). Do not edit generated files.

## Build

```bash
./gradlew build
```

A local build needs `br.fearland:core` in the local Maven repository (`fl plugin build` prepares it).

## Test on staging

After the pull request passes CI and is merged to `dev`:

```bash
tools/fl plugin deploy fearland-<name> --env staging --commit <full-sha-on-origin/dev>
```

Branches, CI and deployment are documented once, in the workspace: `docs/current/deployment.md`.
