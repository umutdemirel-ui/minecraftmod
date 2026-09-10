package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Item registry.
 *
 * <p>Block items are registered here (never inside {@link ModBlocks}) so that block and item
 * registration stay independent: a block can exist without a matching block item and vice versa.</p>
 */
public final class ModItems {

    private ModItems() {
    }

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, UltimateLife.MOD_ID);

    public static final RegistryObject<Item> ULTIMATE_TEST_BLOCK_ITEM = ITEMS.register(
            "ultimate_test_block",
            () -> new BlockItem(ModBlocks.ULTIMATE_TEST_BLOCK.get(), new Item.Properties()));
}
