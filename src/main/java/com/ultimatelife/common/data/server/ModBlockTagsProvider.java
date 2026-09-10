package com.ultimatelife.common.data.server;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Generates {@code data/ultimatelife/tags/block/*.json}.
 *
 * <p>Both vanilla tags (mineable/needs tool) and the mod's own tags go here. Item, entity type and
 * fluid tag providers follow the same pattern.</p>
 */
public final class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(final PackOutput output,
                                final CompletableFuture<HolderLookup.Provider> lookupProvider,
                                final ExistingFileHelper existingFiles) {
        super(output, lookupProvider, UltimateLife.MOD_ID, existingFiles);
    }

    @Override
    protected void addTags(final HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.ULTIMATE_TEST_BLOCK.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(ModBlocks.ULTIMATE_TEST_BLOCK.get());
    }
}
