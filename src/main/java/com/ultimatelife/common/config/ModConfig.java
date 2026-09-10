package com.ultimatelife.common.config;

import com.ultimatelife.UltimateLife;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Forge configuration of the mod.
 *
 * <p>Settings are grouped in sections ({@code general}, {@code network}, ...) so that future systems
 * (furniture, backpack, storage, NPC, economy, vehicles, gameplay) can add their own section here
 * without touching the existing ones.</p>
 *
 * <p>Values are cached in static getters: reading a {@link ForgeConfigSpec} value on a hot path is
 * needless locking, so subsystems read the cached primitives instead. Server authoritative behaviour
 * must never depend on a client side value: this is a COMMON config, and in multiplayer the values
 * that matter are the ones loaded on the server.</p>
 */
@Mod.EventBusSubscriber(modid = UltimateLife.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue LOG_STARTUP;
    public static final ForgeConfigSpec.IntValue MAX_SERVER_PACKETS_PER_SECOND;
    public static final ForgeConfigSpec.BooleanValue LOG_THROTTLED_PACKETS;

    public static final ForgeConfigSpec SPEC;

    static {
        BUILDER.push("general");
        LOG_STARTUP = BUILDER
                .comment("Write a short bootstrap line to the log while the mod is loading.")
                .define("logStartup", true);
        BUILDER.pop();

        BUILDER.push("network");
        MAX_SERVER_PACKETS_PER_SECOND = BUILDER
                .comment("Maximum number of server-bound Ultimate Life packets accepted per player, per second.",
                        "Packets above this budget are dropped on the server before any payload logic runs.",
                        "Raise it only if a future system legitimately needs a higher throughput.")
                .defineInRange("maxServerPacketsPerSecond", 64, 1, 4096);
        LOG_THROTTLED_PACKETS = BUILDER
                .comment("Log packets that were dropped because of the limit above.")
                .define("logThrottledPackets", true);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    private static boolean logStartup = true;
    private static int maxServerPacketsPerSecond = 64;
    private static boolean logThrottledPackets = true;

    private ModConfig() {
    }

    public static void register(final FMLJavaModLoadingContext context) {
        context.registerConfig(Type.COMMON, SPEC);
    }

    public static boolean logStartup() {
        return logStartup;
    }

    public static int maxServerPacketsPerSecond() {
        return maxServerPacketsPerSecond;
    }

    public static boolean logThrottledPackets() {
        return logThrottledPackets;
    }

    @SubscribeEvent
    static void onConfigChanged(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }

        logStartup = LOG_STARTUP.get();
        maxServerPacketsPerSecond = MAX_SERVER_PACKETS_PER_SECOND.get();
        logThrottledPackets = LOG_THROTTLED_PACKETS.get();
    }
}
