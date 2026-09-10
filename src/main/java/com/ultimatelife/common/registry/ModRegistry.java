package com.ultimatelife.common.registry;

import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Single place where every {@link net.minecraftforge.registries.DeferredRegister} of the mod is
 * attached to the mod event bus.
 *
 * <p>Each content family owns its own register class ({@link ModBlocks}, {@link ModItems}, ...);
 * this class only wires them. Adding a new registry means adding one line here, not touching
 * existing registry classes.</p>
 */
public final class ModRegistry {

    private ModRegistry() {
    }

    public static void register(final IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModMenus.MENUS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModParticles.PARTICLE_TYPES.register(modEventBus);
    }
}
