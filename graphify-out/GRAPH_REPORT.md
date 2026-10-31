# Graph Report - tpl  (2026-10-31)

## Corpus Check
- 15 files · ~3,681 words
- Verdict: corpus is large enough that graph structure adds value.
- Unclassified: 5 file(s) not represented in the graph (top: .properties 2, (none) 1, .jar 1)

## Summary
- 35 nodes · 28 edges · 12 communities (1 shown, 11 thin omitted)
- Extraction: 89% EXTRACTED · 11% INFERRED · 0% AMBIGUOUS · INFERRED: 3 edges (avg confidence: 0.95)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `eeb24c76`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- FearLand plugin template
- .onEnable
- fearland-deploy.gradle.kts
- ExamplePlugin
- CLAUDE.md

## God Nodes (most connected - your core abstractions)
1. `FearLand plugin template` - 7 edges
2. `ExamplePlugin` - 3 edges
3. `DeployArtifact` - 2 edges
4. `loadDeployTargets()` - 2 edges
5. `FearLand plugin: engineering rules` - 2 edges
6. `GitHub secrets` - 2 edges
7. `fearland-example` - 1 edges
8. `Create a new plugin` - 1 edges
9. `Deployment targets` - 1 edges
10. `GitHub environments` - 1 edges

## Surprising Connections (you probably didn't know these)
- None detected - all connections are within the same source files.

## Import Cycles
- None detected.

## Communities (12 total, 11 thin omitted)

### Community 0 - "FearLand plugin template"
Cohesion: 0.22
Nodes (8): Branch and deployment flow, Create a new plugin, Deployment targets, FearLand plugin template, GitHub environments, GitHub secrets, Verify a new repository, What is included

## Knowledge Gaps
- **11 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ExamplePlugin` connect `ExamplePlugin` to `.onEnable`?**
  _High betweenness centrality (0.021) - this node is a cross-community bridge._