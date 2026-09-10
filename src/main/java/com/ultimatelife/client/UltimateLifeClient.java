package com.ultimatelife.client;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.config.ModConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client side bootstrap.
 *
 * <p>Annotated with {@code value = Dist.CLIENT}, so a dedicated server never loads this class. Every
 * client-only registration (screens, renderers, key bindings, particle factories, render layers...)
 * is triggered from here, and nothing under {@code com.ultimatelife.common} references this package.</p>
 */
@Mod.EventBusSubscriber(modid = UltimateLife.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class UltimateLifeClient {

    private UltimateLifeClient() {
    }

    @SubscribeEvent
    static void onClientSetup(final FMLClientSetupEvent event) {
        // enqueueWork moves the lambda to the client thread, which is where client state may be touched.
        event.enqueueWork(() -> {
            if (ModConfig.logStartup()) {
                UltimateLife.LOGGER.info("Ultimate Life client runtime ready");
            }
        });
    }
}
