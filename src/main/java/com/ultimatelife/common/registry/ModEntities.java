package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Entity type registry.
 *
 * <p>Empty in Phase 1: the NPC and vehicle systems will add their entities here without touching
 * any other class. Entities are registered on {@link ModRegistry} like every other registry.</p>
 */
public final class ModEntities {

    private ModEntities() {
    }

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, UltimateLife.MOD_ID);
}
