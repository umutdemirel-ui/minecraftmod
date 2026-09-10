package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Block entity type registry.
 *
 * <p>Empty in Phase 1. Furniture, storage and mail systems will register their block entities here;
 * their renderers stay in {@code com.ultimatelife.client.render} so the server never loads them.</p>
 */
public final class ModBlockEntities {

    private ModBlockEntities() {
    }

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, UltimateLife.MOD_ID);
}
