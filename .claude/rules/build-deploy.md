---
paths:
  - "*.gradle.kts"
  - "deploy-targets.json"
---

`build` e `check` nunca copiam jars para o runtime. O deploy local é do `fl`: `fl plugin deploy <plugin> --env local [--dry-run]`; `./gradlew deployTestServer` só delega nele (`--use-built`). Lê `deploy-targets.json` no repo (ou módulo).

Destinos são os servidores que o ambiente tem (`fearland-server/servers/<nome>/plugins`; hoje `velocity`, `login`, `lobby`, `smp`, `eventos`, `builds`). Destino em falta fica `skipped` no plano, nunca é criado.

`-PfearlandServerDir` ou `FEARLAND_SERVER_DIR` escolhem outro fearland-server (o path tem de existir, senão falha; nunca cai para outro destino). O deploy local faz backup, reinicia, verifica saúde e repõe os ficheiros em caso de falha.

Seleccionar o artefacto pela task Gradle correcta (`archiveFile`). Prefixo exacto (`prefix.jar` ou `prefix-<versão>.jar`). O jar antigo vai para backup e só é removido com o novo a instalar; falha faz rollback.

Não inventar destinos para stubs. `network-paper` não recebe destinos nem distribuição só por simetria.
