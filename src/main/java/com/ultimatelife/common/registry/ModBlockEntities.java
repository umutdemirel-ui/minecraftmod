package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Block entities owned by Ultimate Life.
 *
 * <p>Empty in Phase 1 - it exists so that storage/furniture systems have a stable home and
 * so {@link ModRegistry} can wire every registry from one list.
 */
public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, UltimateLife.MOD_ID);

    private ModBlockEntities() {
    }
}
