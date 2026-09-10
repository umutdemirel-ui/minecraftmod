package com.ultimatelife;

import com.mojang.logging.LogUtils;
import com.ultimatelife.common.config.ModConfig;
import com.ultimatelife.common.network.ModNetwork;
import com.ultimatelife.common.registry.ModRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Entry point of the Ultimate Life mod for Minecraft Forge 1.21.1.
 *
 * <p>This class is intentionally thin: it wires the subsystem bootstraps together and holds the
 * constants every other package needs. Game logic never lives here.</p>
 *
 * <p>Layering rules used across the whole mod:</p>
 * <ul>
 *     <li>{@code com.ultimatelife.common.**} runs on both sides and is the only thing a dedicated
 *     server is allowed to load.</li>
 *     <li>{@code com.ultimatelife.client.**} is client-only. It is registered through
 *     {@code @EventBusSubscriber(value = Dist.CLIENT)} and no common class references it.</li>
 *     <li>Systems (furniture, backpack, storage, npc, mail, economy, vehicle, character) talk to
 *     each other only through the registry, config and network layers, never through direct
 *     class references, so they can be added or removed independently.</li>
 * </ul>
 */
@Mod(UltimateLife.MOD_ID)
public final class UltimateLife {

    /** Must match {@code mod_id} in gradle.properties and {@code modId} in mods.toml. */
    public static final String MOD_ID = "ultimatelife";

    /** Human readable mod name, kept in sync with {@code mod_name} in gradle.properties. */
    public static final String MOD_NAME = "Ultimate Life";

    /** Direct slf4j logger, shared by every subsystem. */
    public static final Logger LOGGER = LogUtils.getLogger();

    public UltimateLife(final FMLJavaModLoadingContext context) {
        final IEventBus modEventBus = context.getModEventBus();

        // Config first: the layers below read cached config values while they initialise.
        ModConfig.register(context);

        ModRegistry.register(modEventBus);
        ModNetwork.register();

        modEventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        if (ModConfig.logStartup()) {
            LOGGER.info("{} [{}] bootstrap complete", MOD_NAME, MOD_ID);
        }
    }
}
