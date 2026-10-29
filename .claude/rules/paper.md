---
paths:
  - "src/main/java/**/*.java"
---

Paper: `JavaPlugin`. Comandos via CommandAPI 12 (`CommandAPICommand` + `.register(plugin)`). Nunca `CommandExecutor`, `getCommand()`, nem declaração no `plugin.yml`.

Árvore: `withArguments` / `withOptionalArguments` / `withSubcommand`. Texto: `/comando <obrigatório> [opcional]`. `withUsage` só se for texto útil; omitir por defeito.

Bootstrap `onEnable`: configs → `Services.require` → migrate → domínio → `Services.register` → listeners → comandos. InvUI: `setPlugin` antes dos menus.

Compile `br.fearland:core` como `compileOnly`. Runtime: jar `fearland-api`. Sem `FrameworkPlugin`, `CustomCommand`, `YamlConfig`, `ViewsAPI`, `TaskScheduler`.

JDBC/HTTP fora do platform thread. Voltar ao Bukkit com `Bukkit.getScheduler().runTask(plugin, ...)`.

Base viva de comandos: `HeartsAdminCommand` (lifesteal). Não copiar `/home`+`visibility`, `/kit`+`edit`, `/duelo`+`aceitar`, nem o chat.
