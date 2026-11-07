# FearLand plugin template

Starting point for a new first-party FearLand **Paper** plugin: the Gradle build, a minimal plugin and the
deployment-target contract (`deploy-targets.json`) and the pull-request build caller (`.github/workflows/build.yml`). Flow, branches
and production releases are documented once, in the workspace: `docs/deployment.md`.

## New plugin

1. **Use this template** (GitHub: *Use this template*), name the repository `FearServers/fearland-<name>`, clone it into
   `plugins/` of the FearLand workspace and create `dev` from `main`.
2. Give it its identity (a Velocity plugin is not covered by this template):
   - `settings.gradle.kts`: `rootProject.name = "fearland-<name>"`;
   - rename the package `br.fearland.example` and `ExamplePlugin`;
   - `src/main/resources/plugin.yml`: `name`, `main`, `description` (keep `depend`);
   - `deploy-targets.json`: `id` and `prefix` are the jar base name (`fearland-<name>`); `destinations` are servers that
     exist in `fearland-server/servers/` (or are listed in its `deferred-servers`).
   - `.github/workflows/build.yml`: add the sibling plugins you compile from source under `siblings` (usually none).
3. **Owner**: add a `[[repo]]` entry (`kind = "plugin"`, `lifecycle = "active"`) to `tools/workspace.toml` of the workspace, then run
   `tools/fl ci sync` (sets the `FEARLAND_CHECKOUT_TOKEN` Actions secret on the new repository).
4. **Developer**: build, test locally, then try it on staging.

## Build and check

```bash
./gradlew build          # compile, jar; never deploys
```

A build needs `br.fearland:core` and `br.fearland:paper` in the local Maven repository: `tools/fl workspace setup` publishes
`fearland-api` there.

Before a staging deploy: copy the jar into the matching local server (`fearland-server/ops/local/dev restart <server>`) and
confirm in the log that the plugin is enabled.

## Test on staging

```bash
tools/fl deploy          # from this directory: pushes the branch, builds once on GitHub, installs on staging, restarts
./gradlew deployStaging  # `fl deploy --force-send` (ssh, no GitHub) for the IDE Gradle panel (group: deployment); -PnoRestart installs without restarting
```

`deployStaging` only runs `fl deploy --force-send` (`..\..\tools\fl.cmd` on Windows): it holds no credential and is not part of `build`.
Production is released by `fl promote`, which copies what is on staging.

## CI

No CI runs for `dev`. `build.yml` builds a pull request to `main` once it is ready for review (not a draft, not docs only) and
cancels the older run when new commits arrive. The merge is not built again.
