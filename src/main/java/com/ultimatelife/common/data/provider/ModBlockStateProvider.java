package com.ultimatelife.common.data.provider;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Generates blockstates and block models.
 *
 * <p>Runs on the client side of data generation only. Models are described in code so a
 * new block cannot ship without one - a missing model is a compile-time concern instead of
 * a runtime purple/black cube.
 */
public final class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(final PackOutput output, final ExistingFileHelper existingFiles) {
        super(output, UltimateLife.MOD_ID, existingFiles);
    }

    @Override
    protected void registerStatesAndModels() {
        // -- Test harness (Phase 1) --------------------------------------
        simpleCube(ModBlocks.ULTIMATE_TEST_BLOCK.get());
    }

    /** Registers a plain cube-all blockstate plus the matching block item model. */
    private void simpleCube(final Block block) {
        final String path = BuiltInRegistries.BLOCK.getKey(block).getPath();
        simpleBlockWithItem(block, models().cubeAll(path, blockTexture(block)));
    }
}
