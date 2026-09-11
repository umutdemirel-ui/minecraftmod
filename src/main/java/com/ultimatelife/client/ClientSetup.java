package com.ultimatelife.client;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.config.UltimateLifeConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-only bootstrap.
 *
 * <p>Everything under {@code com.ultimatelife.client} is loaded <em>only</em> on the
 * physical client: this class is discovered through a {@code Dist.CLIENT}-gated
 * {@code @Mod.EventBusSubscriber}, so a dedicated server never resolves the annotation
 * target and never loads a client class. Keep it that way - do not reference anything in
 * this package from {@code com.ultimatelife.common}, and do not call into client classes
 * from common code without a {@code Dist} check.
 *
 * <h2>Where client registrations go</h2>
 * <ul>
 *   <li>{@code client.screen} - {@code MenuScreen}s, registered here with
 *       {@code MenuScreens.register} inside {@code enqueueWork};</li>
 *   <li>{@code client.render} - entity/block-entity renderers via
 *       {@code EntityRenderersEvent.RegisterRenderers};</li>
 *   <li>{@code client.model} - geometry/layer definitions;</li>
 *   <li>{@code client.particle} - {@code ParticleProvider}s via
 *       {@code RegisterParticleProvidersEvent};</li>
 *   <li>{@code client.controller} - input handling (key mappings, vehicle controls);</li>
 *   <li>{@code client.gui} - reusable widgets and overlays.</li>
 * </ul>
 */
@Mod.EventBusSubscriber(modid = UltimateLife.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ClientSetup {

    private ClientSetup() {
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        // enqueueWork keeps registrations that are not thread-safe off the parallel
        // mod-loading thread.
        event.enqueueWork(() -> {
            // Phase 1 has nothing to register yet: no menus, no entity renderers and no
            // particle providers exist. Each future system adds its registration here.
        });

        if (UltimateLifeConfig.COMMON.general().startupLogging().get()) {
            UltimateLife.LOGGER.info("{} client setup complete", UltimateLife.MOD_NAME);
        }
    }
}
