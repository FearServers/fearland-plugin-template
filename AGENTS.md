# FearLand plugin: engineering rules

- Paper plugin: `JavaPlugin`; commands with CommandAPI 12 (`CommandAPICommand(...).register(plugin)`), never `plugin.yml` commands.
- Compile `br.fearland:core` as `compileOnly`; never shade FearLand. InvUI is the only bundled exception.
- `onEnable` order: configs, `Services.require`, migrate, domain, `Services.register`, listeners, commands.
- Database: `Services.require(Database.class)`; the plugin never creates or closes the pool. JDBC and HTTP stay off the platform
  thread; return to Bukkit with `Bukkit.getScheduler().runTask(plugin, ...)`.
- `build`/`check` never deploy. Deploys come from CI: `dev` stages, `main` ships, feature branches and pull requests do not.
- Jar name: `<artifact-id>-<branch>-<short-commit>.jar`. Never commit jars, secrets, `.env` files or server data.
- Do not invent APIs or signatures; confirm the real contract. Validate with `./gradlew build` before pushing.
