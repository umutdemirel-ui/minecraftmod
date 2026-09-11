package com.ultimatelife;

import com.mojang.logging.LogUtils;
import com.ultimatelife.common.config.UltimateLifeConfig;
import com.ultimatelife.common.event.ModCommonEvents;
import com.ultimatelife.common.network.ModNetwork;
import com.ultimatelife.common.registry.ModRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Entry point of the Ultimate Life mod.
 *
 * <p>Deliberately thin: this class only wires the sub-systems together during mod
 * construction. No game logic lives here, so every system stays independently testable
 * and can grow without touching the bootstrap.
 *
 * <p>Construction happens on a mod-loading worker thread, once per side. Config values are
 * <em>not</em> readable yet at that point - Forge only loads the config files after every
 * mod has been constructed - so anything that depends on config runs in
 * {@link #commonSetup(FMLCommonSetupEvent)} instead.
 *
 * <p>Anything that must happen on the client only is delegated to
 * {@code com.ultimatelife.client.ClientSetup}, which Forge discovers through a
 * {@code Dist.CLIENT}-gated event subscriber. That keeps client classes off the dedicated
 * server's classpath entirely.
 */
@Mod(UltimateLife.MOD_ID)
public final class UltimateLife {

    /** Mod id, must match the entry in {@code META-INF/mods.toml}. */
    public static final String MOD_ID = "ultimatelife";

    /** Human readable mod name, used for logging and UI fallbacks. */
    public static final String MOD_NAME = "Ultimate Life";

    public static final Logger LOGGER = LogUtils.getLogger();

    public UltimateLife(final FMLJavaModLoadingContext context) {
        final IEventBus modBus = context.getModEventBus();

        // Config first: sub-systems read it once it is loaded, not here.
        context.registerConfig(ModConfig.Type.COMMON, UltimateLifeConfig.COMMON_SPEC);

        // Every DeferredRegister in the mod attaches itself to the mod bus from here.
        ModRegistry.init(modBus);

        // Builds the network channel so Forge sees it while the mod is loading.
        ModNetwork.init(modBus);

        // Game (non-mod) bus events that are safe on both physical sides.
        MinecraftForge.EVENT_BUS.register(ModCommonEvents.class);

        modBus.addListener(this::commonSetup);
    }

    /** Runs on both physical sides after configs have been loaded. */
    private void commonSetup(final FMLCommonSetupEvent event) {
        if (UltimateLifeConfig.COMMON.general().startupLogging().get()) {
            LOGGER.info("{} initialised - Forge 1.21.1, Phase 1 foundation", MOD_NAME);
        }
    }
}
