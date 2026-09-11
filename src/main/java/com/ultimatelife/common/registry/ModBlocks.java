package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.utility.UltimateTestBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Every block owned by Ultimate Life.
 *
 * <p>Future systems (furniture, storage, devices) register their blocks here, grouped by
 * system with a comment banner. Item forms live in {@link ModItems}; block entities in
 * {@link ModBlockEntities}.
 */
public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, UltimateLife.MOD_ID);

    private ModBlocks() {
    }

    // ------------------------------------------------------------------ //
    //  Test harness (Phase 1) - safe to delete once the foundation is proven
    // ------------------------------------------------------------------ //

    /**
     * Throwaway block used to validate the registry, blockstate, model, texture, loot
     * table and creative-tab pipeline end to end.
     */
    public static final RegistryObject<Block> ULTIMATE_TEST_BLOCK = register(
        "ultimate_test_block",
        () -> new UltimateTestBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_PURPLE)
            .strength(2.0F, 6.0F)
            .sound(SoundType.METAL))
    );

    private static RegistryObject<Block> register(final String name, final Supplier<Block> factory) {
        return BLOCKS.register(name, factory);
    }
}
