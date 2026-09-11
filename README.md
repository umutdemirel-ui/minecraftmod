# Ultimate Life — Forge 1.21.1

Foundation of the **Ultimate Life** mod, rebuilt from scratch for **Minecraft Forge**.
This repository is independent of the previous Fabric project; nothing here depends on
Fabric Loader, Fabric API or generated (MCreator) code.

| | |
|---|---|
| Minecraft | 1.21.1 |
| Loader | Forge 52.1.16 |
| Java | 21 |
| Build | Gradle (ForgeGradle 7) |
| Mod id | `ultimatelife` |

---

## Phase 1 scope

Phase 1 is the *foundation*, not content. What ships:

- **Bootstrap** — `UltimateLife` is a thin constructor that wires config, registries,
  networking and events together and nothing else.
- **Registry** — one `DeferredRegister` per registry, all attached from `ModRegistry`.
- **Config** — Forge `ForgeConfigSpec` with a sectioned layout that future systems extend
  by adding a holder class.
- **Networking** — a versioned Forge `Channel` over vanilla `CustomPacketPayload`s, with a
  per-player token-bucket rate limiter that runs on the network thread.
- **Data generation** — blockstates/models, loot tables, recipes, tags and advancements.
- **Creative tab** — a single `Ultimate Life` tab whose contents are contributed by an
  event, so each system appends its own entries.
- **Test harness** — `ultimate_test_block`, a plain cube that exercises the whole pipeline
  at runtime (registry → blockstate → model → texture → loot → tab → network round trip).

Furniture, backpacks, storage, NPCs, mail, economy, vehicles and character progression are
**not** implemented. Their packages exist, documented, as fixed homes for later phases.

---

## Layout

```
src/main/java/com/ultimatelife/
├── UltimateLife.java          mod entry point (bootstrap only)
├── common/                    loaded on both physical sides
│   ├── registry/              one DeferredRegister per registry + ModCreativeTabs
│   ├── config/                ForgeConfigSpec
│   ├── network/               Channel, packets/ (payload records), handler/
│   ├── event/                 game-bus listeners safe on both sides
│   ├── data/                  GatherDataEvent wiring + data providers
│   ├── utility/               PacketGuard, UltimateTestBlock (temporary)
│   └── furniture/ backpack/ storage/ npc/ mail/ economy/ vehicle/ character/
└── client/                    loaded on the physical client only
    ├── ClientSetup.java       Dist.CLIENT-gated bootstrap
    └── gui/ screen/ render/ model/ animation/ controller/ particle/
```

`common` must never reference `client`. Client-only registrations happen through
`@Mod.EventBusSubscriber(value = Dist.CLIENT, ...)`, so a dedicated server never loads a
class from the `client` package.

## Resources

```
src/main/resources/
├── META-INF/mods.toml         filtered by ProcessResources from gradle.properties
├── pack.mcmeta
├── assets/ultimatelife/       hand-written: lang, textures
└── data/ultimatelife/         hand-written worldgen (empty for now)

src/generated/resources/       produced by `./gradlew runData`, committed
```

Blockstates, models, loot tables, recipes, tags and advancements are generated, not
hand-written, so they cannot drift away from the code that registers them.

---

## Building

```bash
./gradlew clean build     # compile + jar -> build/libs/ultimatelife-<version>.jar
./gradlew runData         # regenerate src/generated/resources
./gradlew runClient       # development client
./gradlew runServer       # development dedicated server (--nogui)
```

Requires a JDK 21 on `JAVA_HOME`.

## Verifying the foundation in game

1. Launch the client, open the creative inventory — the **Ultimate Life** tab is the last
   tab before the spawn eggs.
2. Place the **Ultimate Test Block**.
3. Right-click it with an empty hand. The client logs a ping, the server logs
   `[test] ping from <player> at <pos>` and replies; the client then logs
   `[test] pong for <pos> (server time …)`.
4. Break it — the generated loot table drops the block.
