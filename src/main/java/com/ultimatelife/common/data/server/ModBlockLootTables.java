package com.ultimatelife.common.data.server;

import com.ultimatelife.common.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

/**
 * Block drops.
 *
 * <p>{@code getKnownBlocks()} tells the data generator which tables this mod owns, so a missing table
 * fails the generation run instead of silently producing a block that drops nothing.</p>
 */
public final class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables(final HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(ModBlocks.ULTIMATE_TEST_BLOCK.get());
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.ULTIMATE_TEST_BLOCK.get());
    }
}
