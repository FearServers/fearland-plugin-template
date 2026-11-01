# FearLand plugin template

Starting point for a new first-party FearLand Paper plugin. It contains only the Gradle build, a minimal plugin and the
deployment-target contract. CI and agent configuration are **not** part of the template: the FearLand workspace tool
(`fl`) generates them.

## New plugin

1. **Use this template** and create `FearServers/fearland-<name>`; clone it into `plugins/` of the FearLand workspace and
   create `dev` from `main`.
2. Rename the example: `rootProject.name` (`settings.gradle.kts`), the package `br.fearland.example` and main class,
   `src/main/resources/plugin.yml`, and `id`/`prefix` in `deploy-targets.json`.
3. From the workspace: `tools/fl plugin sync fearland-<name>` onboards it (workspace entry, `deploy-targets.json`
   destinations and restart policy, generated CI caller and agent setup, build). Where your workspace's `fl` does not have
   `plugin sync` yet, do it by hand: set `destinations` to servers that exist in `fearland-server/servers/`, add the
   repository to `tools/workspace.toml`, then `tools/fl ci sync fearland-<name>` and `tools/fl claude sync fearland-<name>`.
   Do not edit generated files.

## Build

```bash
./gradlew build
```

A local build needs `br.fearland:core` in the local Maven repository (`fl plugin build` prepares it).

## Test on staging

`tools/fl plugin deploy fearland-<name> --env staging --dry-run` shows the plan; the deploy commands (working tree or an
explicit commit) and what happens on the `dev -> main` merge are documented once, in the workspace:
`docs/current/deployment.md`.
