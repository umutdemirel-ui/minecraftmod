package com.ultimatelife.common.event;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.registry.ModCreativeTabs;
import com.ultimatelife.common.registry.ModItems;
import com.ultimatelife.common.utility.PacketGuard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Game-bus listeners that are safe on <em>both</em> physical sides.
 *
 * <p>Registered explicitly from the mod constructor rather than through
 * {@code @Mod.EventBusSubscriber}, so the wiring stays visible in one place.
 * Client-only listeners live in {@code com.ultimatelife.client} behind a
 * {@code Dist.CLIENT} gate.
 */
public final class ModCommonEvents {

    private ModCommonEvents() {
    }

    /**
     * Fills the Ultimate Life creative tab.
     *
     * <p>Contents are contributed here instead of in the tab's builder so that each system
     * can append its own entries without creating a dependency on
     * {@link ModCreativeTabs}' registration code.
     */
    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != ModCreativeTabs.ULTIMATE_LIFE_TAB.getKey()) {
            return;
        }

        // -- Test harness (Phase 1) --------------------------------------
        event.accept(ModItems.ULTIMATE_TEST_BLOCK_ITEM.get());

        // Future systems append their item suppliers here, one block per system,
        // e.g. furniture, backpacks, storage, NPC spawn items, vehicles, devices.

        UltimateLife.LOGGER.debug("Populated the {} creative tab", ModCreativeTabs.ULTIMATE_LIFE_TAB.getId());
    }

    /** Frees per-player network rate-limit buckets when a player disconnects. */
    @SubscribeEvent
    public static void onPlayerLoggedOut(final PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            PacketGuard.forget(player.getUUID());
        }
    }
}
