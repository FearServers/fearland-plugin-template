---
paths:
  - "**/repository/**"
  - "**/persistence/**"
  - "**/migrations/**"
---

PostgreSQL + Jdbi. Pool da API: `Services.require(Database.class)`. O plugin nunca cria nem fecha a pool.

Repository: POJO, `Handle` como primeiro parâmetro. Sem `JdbcRepository`, sem SQL Object, sem MySQL. SQL em `*Queries` quando há várias statements. Named parameters (`:id`).

O service (ou o caller) dona a transação (`inTransaction` / `*Async`). O repository não agenda e não conhece `Database`.

Migrate no startup: `SchemaMigrator.migrate(database, "modulo", getClass().getClassLoader())`. Aceite no thread de `onEnable`.

Gameplay: `database.*Async(...)` → `Bukkit.getScheduler().runTask(plugin, ...)`. Não decidir negócio por `exception.getMessage()`.

Lifesteal `PlayerLifeRepository` que injeta `Database` é dívida, não modelo.
