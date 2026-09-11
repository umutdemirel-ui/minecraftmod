package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import java.util.List;

/**
 * Single place that knows about every {@link DeferredRegister} in the mod.
 *
 * <p>Each {@code Mod*} class owns the entries for exactly one registry and knows nothing
 * about the others, so systems stay independent. This class only forwards the mod event
 * bus to them during construction.
 */
public final class ModRegistry {

    private static final List<DeferredRegister<?>> REGISTERS = List.of(
        ModBlocks.BLOCKS,
        ModItems.ITEMS,
        ModBlockEntities.BLOCK_ENTITY_TYPES,
        ModEntities.ENTITY_TYPES,
        ModMenus.MENU_TYPES,
        ModCreativeTabs.CREATIVE_TABS,
        ModSounds.SOUND_EVENTS,
        ModParticles.PARTICLE_TYPES
    );

    private ModRegistry() {
    }

    /**
     * Attaches every register to the mod event bus.
     *
     * <p>Must be called exactly once, from the mod constructor, before registry events fire.
     */
    public static void init(final IEventBus modBus) {
        REGISTERS.forEach(register -> register.register(modBus));
        UltimateLife.LOGGER.debug("Registered {} DeferredRegisters", REGISTERS.size());
    }
}
