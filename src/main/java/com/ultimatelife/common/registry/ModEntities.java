package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Entities owned by Ultimate Life (NPCs, vehicles, projectiles...).
 *
 * <p>Empty in Phase 1. Entity <em>renderers</em> belong to {@code com.ultimatelife.client.render}
 * and are registered from a {@code Dist.CLIENT}-gated subscriber only.
 */
public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, UltimateLife.MOD_ID);

    private ModEntities() {
    }
}
