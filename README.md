# FearLand plugin template

Public starting point for a new first-party FearLand Paper plugin.

The template already includes the standard Gradle/JDK setup, Claude/agent files, the FearLand CI caller and the deployment-target contract. A new plugin should only need repository-specific naming, destinations and the standard FearLand GitHub secrets.

## Create a new plugin

1. Click **Use this template** and create the repository under `FearServers` as `fearland-<name>`.
2. Clone it into the FearLand workspace.
3. Rename the example plugin values:
   - `rootProject.name` in `settings.gradle.kts`;
   - `group`/Java package `br.fearland.example`;
   - Java package path and main class;
   - `src/main/resources/plugin.yml` (`name`, `main`, `description`, etc.);
   - artifact `id` and `prefix` in `deploy-targets.json`.
4. Set the destination servers in `deploy-targets.json`.
5. Add the repository to the active repository catalog in the private `FearServers/fearland` workspace when required by the workspace tooling.
6. Create `dev` from `main`.
7. From the private FearLand meta workspace, run:

```bash
tools/fl ci sync fearland-<name>
tools/fl claude sync fearland-<name>
```

`fl ci sync` is important: the public template starts with the shared workflow on `@main` only as a bootstrap. The sync command replaces it with the current pinned FearLand CI revision used by active plugins.

Do this before the first real CI rollout.

## Deployment targets

`deploy-targets.json` is the plugin-owned deployment contract.

Example:

```json
{
  "artifacts": [
    {
      "id": "fearland-example",
      "platform": "paper",
      "task": "jar",
      "prefix": "fearland-example",
      "destinations": ["lobby"]
    }
  ]
}
```

Valid server destinations are currently:

- `velocity`
- `login`
- `lobby`
- `smp`
- `eventos`
- `builds`

For a multi-artifact repository, give every artifact a distinct stable id, for example `fearland-chat-paper` and `fearland-chat-velocity`.

CI names deployable jars as:

```text
<artifact-id>-<branch>-<short-commit>.jar
```

The deployer only replaces older jars belonging to that same artifact id.

## GitHub secrets

The workflow uses `secrets: inherit`, matching active FearLand plugin repositories.

Configure these standard Actions secrets on the new repository:

| Secret | Purpose |
|---|---|
| `FEARLAND_CHECKOUT_TOKEN` | Read access to the private FearLand meta/API/server/sibling repositories used by CI. |
| `FEARLAND_PTERODACTYL_CONFIG_PRODUCTION` | Production server/Pterodactyl deployment configuration consumed by `fl`. |
| `FEARLAND_PTERODACTYL_KEY_PRODUCTION` | Restricted production Pterodactyl Client API key used for Minecraft deployment operations. |
| `FEARLAND_PTERODACTYL_CONFIG_STAGING` | Staging deployment configuration. Configure when staging is available. |
| `FEARLAND_PTERODACTYL_KEY_STAGING` | Restricted staging Pterodactyl Client API key. Configure when staging is available. |

Do not commit any of these values to Git.

The Pterodactyl keys should belong to the restricted project/Minecraft operator boundary, not an unrestricted Panel/Application admin identity.

### Setup with `gh`

Set the repository once:

```bash
REPO="FearServers/fearland-<name>"
```

Then configure the common private-repository checkout token:

```bash
gh secret set FEARLAND_CHECKOUT_TOKEN --repo "$REPO"
```

Configure production using the existing production values:

```bash
gh secret set FEARLAND_PTERODACTYL_CONFIG_PRODUCTION --repo "$REPO" < /path/to/production-environment.yml
gh secret set FEARLAND_PTERODACTYL_KEY_PRODUCTION --repo "$REPO"
```

`gh` prompts for the secret value when no value is supplied on the command line, so the key does not need to appear in shell history.

When the staging VPS is ready:

```bash
gh secret set FEARLAND_PTERODACTYL_CONFIG_STAGING --repo "$REPO" < /path/to/staging-environment.yml
gh secret set FEARLAND_PTERODACTYL_KEY_STAGING --repo "$REPO"
```

Until staging is configured, a deploy job on `dev` is expected to fail closed with `staging not configured`. Build/test still provides the useful CI result.

### GitHub environments

The shared workflow labels deploy jobs with the GitHub environments:

- `dev` -> `staging`
- `main` -> `production`

The deployment secrets above are repository-level today for compatibility with the current FearServers organization setup. If the organization later uses private-repository environment secrets, the same secret names can be moved into the matching `staging` and `production` environments without changing the plugin contract.

## Branch and deployment flow

```text
feature/* -> PR -> dev -> staging -> PR dev->main -> main -> production
```

- feature branch / ordinary PR: build and test only;
- push or merge to `dev`: build/test, produce `*-dev-<sha>.jar`, deploy to staging;
- PR `dev -> main`: build/test only;
- push or merge to `main`: build/test, produce `*-main-<sha>.jar`, deploy to production.

A missing staging configuration is allowed to fail closed while staging is not yet provisioned. Production must not silently skip deployment when its configuration is missing.

## Verify a new repository

Before considering the plugin ready:

```bash
./gradlew build
```

Then confirm:

```bash
gh secret list --repo "$REPO"
```

The list should contain the standard FearLand secrets that are currently applicable.

After `fl ci sync`, inspect `.github/workflows/fearland.yml` and confirm it uses the pinned shared FearLand workflow revision and `secrets: inherit`.

Push to `dev` only after the repository-specific names, deployment targets and secrets are correct.

## What is included

| Path | Purpose |
|---|---|
| `build.gradle.kts`, `gradle.properties`, `gradle/` | Gradle build, approved dependency versions and wrapper |
| `src/main/java`, `src/main/resources/plugin.yml` | Minimal Paper plugin |
| `deploy-targets.json` | Artifact identity and destination servers |
| `.github/workflows/fearland.yml` | Bootstrap caller for the shared FearLand CI pipeline |
| `CLAUDE.md`, `AGENTS.md`, `.claude/` | Project instructions for Claude Code and other agents |

The build compiles against the private FearLand API where required. The shared pipeline checks out/publishes that dependency for CI; a standalone local checkout may require the API in the local Maven repository.
