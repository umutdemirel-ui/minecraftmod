# Ultimate Life — Forge 1.21.1 (Faz 1)

Bu depo, Ultimate Life modunun **Minecraft Forge 1.21.1** üzerindeki temelini içerir.
Faz 1 kapsamı: sağlam bir Forge iskeleti. Furniture / Backpack / Storage / NPC / Mail / Economy /
Vehicle / Character sistemleri bu fazda **yazılmadı**; sadece mimaride yerleri ayrıldı
(`package-info.java` dosyalarıyla).

| Bileşen | Değer |
|---|---|
| Minecraft | 1.21.1 |
| Loader | Forge 52.1.14 (loader range `[52,)`) |
| Java | 21 |
| Gradle | 8.14.3 (wrapper) + ForgeGradle `[6.0.24,6.2)` |
| Mod ID | `ultimatelife` |
| Mod Name | Ultimate Life |
| Group | `com.ultimatelife` |

## Hızlı başlangıç

```bash
./gradlew build          # derle + jar üret (build/libs/ultimatelife-1.0.0.jar)
./gradlew runClient      # geliştirme istemcisi
./gradlew runServer      # geliştirme sunucusu (dedicated)
./gradlew runData        # veri üretimi: blockstate, model, lang, recipe, loot, tag
```

İlk çalıştırmada ForgeGradle Minecraft'ı indirip decompile eder (5–15 dk). Daha sonraki
çalıştırmalar Gradle cache'i sayesinde hızlıdır.

## Paket mimarisi

```
com.ultimatelife
├── UltimateLife.java              # @Mod giriş noktası (sadece bağlama yapar)
├── common                         # HER İKİ TARAFTA çalışır, sunucu bunu yükler
│   ├── registry                   # DeferredRegister sınıfları + ModRegistry (kayıt bağlama)
│   │   ├── ModBlocks, ModItems, ModCreativeTabs
│   │   ├── ModBlockEntities, ModEntities, ModMenus, ModSounds, ModParticles
│   ├── config/ModConfig           # ForgeConfigSpec, bölümlere ayrılmış (general / network)
│   ├── network
│   │   ├── ModNetwork             # tek, sürümlenmiş payload kanalı: ultimatelife:main
│   │   ├── handler/PacketRateLimiter  # sunucu tarafı, oyunucu başına sliding-window
│   │   └── packets                # (Faz 2+) paket record'ları
│   ├── data                       # GatherDataEvent + client/server provider'ları
│   ├── furniture, backpack, storage, npc, mail, economy, vehicle, character, utility
│   │   └── (Faz 1'de yalnızca package-info — yer ayrıldı, içerik yok)
└── client                         # SADECE İSTEMCİ — dedicated server asla yüklemez
    ├── UltimateLifeClient         # Dist.CLIENT bootstrap
    └── gui, screen, render, model, animation, controller, particle
        └── (Faz 1'de yalnızca package-info)
```

Bağımlılık kuralı: sistemler birbirini doğrudan tanımaz. İletişim `registry`, `config` ve
`network` katmanları üzerinden yürür; böylece bir sistem diğerini bozmadan eklenebilir/çıkarılabilir.

## Tasarım kararları

**Registry.** Her içerik ailesinin kendi `DeferredRegister`'ı var; hepsi tek bir yerde
(`ModRegistry.register`) mod event bus'ına bağlanıyor. Yeni bir registry eklemek = `ModRegistry`'ye
bir satır.

**Config.** Tek bir COMMON spec, `general` ve `network` bölümleriyle. Değerler statik alanlara
cache'lenir (hot-path'te spec okunmaz). Faz 1'de sadece gerçekten kullanılan 3 ayar var:
`general.logStartup`, `network.maxServerPacketsPerSecond`, `network.logThrottledPackets`.
Yeni sistemler kendi `BUILDER.push("...")` bölümünü ekler.

**Networking.** Forge 1.21.1'in `ChannelBuilder` + `PayloadChannel` API'si. Bütün client→server
paketler `ModNetwork.registerServerbound(...)` üzerinden kaydedilir ve şu kapıdan geçer:
`sender != null` (sunucu tarafı) → rate limiter → handler. Paket `setPacketHandled(true)` ile
işaretlenir; level'e dokunmak isteyen handler `enqueueWork` kullanmak zorundadır.
Faz 1'de hiç paket yok: gereksiz paket eklenmedi.

**Client/server ayrımı.** `com.ultimatelife.client` altındaki her şey `@EventBusSubscriber(value =
Dist.CLIENT)` ile tutturulur; `common` altından `client` paketine tek bir referans bile yoktur.
Bunu doğrulamanın yolu `./gradlew runServer` (CI'da otomatik koşuyor).

**Data generation.** El yazımı JSON yok: blockstate, model, item model, `en_us` lang, recipe,
loot table ve block tag'leri `./gradlew runData` üretiyor ve `src/generated/resources` içine
yazıyor (jar'a dahil). CI, üretilen çıktının commit'lenmiş hâlle aynı olduğunu kontrol eder.

## Faz 1 test bloğu

`ultimate_test_block` bilinçli olarak bırakıldı; tüm zinciri uçtan uca doğrular:
registry → blockstate/model üretimi → texture → loot table → recipe → tag → creative tab.
Faz 2'de ya silinir ya da gerçek bir bloğa dönüşür.

## CI

`.github/workflows/build.yml` dört iş koşar:

1. **build** — `./gradlew build` + jar artifact.
2. **datagen** — `./gradlew runData` ve üretilen kaynakların güncel olup olmadığının kontrolü.
3. **server** — dedicated sunucuyu EULA kabul edilmiş şekilde başlatır, `Ultimate Life [ultimatelife]
   bootstrap complete` ve `Done (` satırlarını arar, log'u artifact olarak yükler.
   (Bu iş aynı zamanda client/server ayrımının kanıtıdır: sunucu `com.ultimatelife.client` altındaki
   hiçbir sınıfı yüklemeden açılıyor.)
4. **client** — xvfb + llvmpipe ile başsız (headless) istemci açar; `Ultimate Life client runtime
   ready` satırını arar (bu satır, oyunun pencere/GL bağlamını kurup modu yüklediğini ve yalnızca
   istemcide çalışan kurulum kodunu çalıştırdığını kanıtlar). Ses cihazı olmadığı için ana menü
   işareti bulunamazsa uyarı verir, hata vermez.
