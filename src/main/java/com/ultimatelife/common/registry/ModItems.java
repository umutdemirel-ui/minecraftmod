package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Every item owned by Ultimate Life.
 *
 * <p>Item forms of blocks are created through {@link #blockItem(RegistryObject)} so that
 * the id always mirrors the block id - the game resolves block-item relations by id.
 */
public final class ModItems {

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, UltimateLife.MOD_ID);

    private ModItems() {
    }

    // ------------------------------------------------------------------ //
    //  Test harness (Phase 1)
    // ------------------------------------------------------------------ //

    public static final RegistryObject<Item> ULTIMATE_TEST_BLOCK_ITEM = blockItem(ModBlocks.ULTIMATE_TEST_BLOCK);

    /**
     * Registers a plain {@link BlockItem} under the same path as the given block.
     *
     * <p>Systems that need custom item behaviour (backpacks, tools, vehicles) register
     * their items directly and pass their own {@link Item.Properties}.
     */
    private static RegistryObject<Item> blockItem(final RegistryObject<? extends net.minecraft.world.level.block.Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
