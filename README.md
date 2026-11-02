# FearLand plugin template

Starting point for a new first-party FearLand **Paper** plugin: the Gradle build, a minimal plugin and the
deployment-target contract (`deploy-targets.json`). CI and agent configuration are **not** in this repository; the
FearLand workspace tool `fl` generates them when the plugin is onboarded. Flow, branches and production releases are
documented once, in the workspace: `docs/current/deployment.md`.

## New plugin

1. **Use this template** (GitHub: *Use this template*), name the repository `FearServers/fearland-<name>`, clone it into
   `plugins/` of the FearLand workspace and create `dev` from `main`.
2. Give it its identity (a Velocity plugin is not covered by this template):
   - `settings.gradle.kts`: `rootProject.name = "fearland-<name>"`;
   - rename the package `br.fearland.example` and `ExamplePlugin`;
   - `src/main/resources/plugin.yml`: `name`, `main`, `description` (keep `depend`);
   - `deploy-targets.json`: `id` and `prefix` are the jar base name (`fearland-<name>`); `destinations` are servers that
     exist in `fearland-server/servers/`.
3. **Owner**, inside the repository: `fl sync`. It adds the workspace entry (`tools/workspace.toml`), validates
   `deploy-targets.json`, generates the CI caller (`.github/workflows/fearland.yml`) and the agent files, sets the
   `FEARLAND_CHECKOUT_TOKEN` Actions secret and builds. It needs the owner's SSH key (`tools/README.md`). Generated files
   are never edited by hand; run `fl sync` again to repair them.
4. **Developer**, inside the repository: build, test and try it on staging.

## Build and check

```bash
./gradlew build          # compile, jar; never deploys
fl doctor                # is this machine ready for the plugin you are in
```

A build needs `br.fearland:core` and `br.fearland:paper` in the local Maven repository; `fl deploy` publishes
`fearland-api` there when it is missing.

## Test on staging

```bash
fl deploy --dry-run      # the plan only
fl deploy                # build, install on staging, restart, prove the plugin loaded
fl deploy --status       # what is installed
./gradlew deployStaging  # the same `fl deploy`, for the IDE Gradle panel (group: deployment)
```

`deployStaging` is a thin wrapper: it runs `fl deploy` in this directory (`../../tools/fl` of the workspace, else `fl` from
`PATH`) and is not part of `build`. Staging is the only target; production is released from `fearland-server`.

## What exists only after onboarding

Until the owner has run `fl sync`: no CI on pull requests (the caller and the checkout secret do not exist yet), no
entry in the workspace inventory, and `fl deploy` has no destinations to resolve. The template itself builds, but only
inside a workspace that provides `fearland-api` (`fl deploy` and `fl doctor` need one).
