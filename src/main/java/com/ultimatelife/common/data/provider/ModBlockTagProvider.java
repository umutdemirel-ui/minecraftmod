package com.ultimatelife.common.data.provider;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

/**
 * Generates block tags.
 *
 * <p>Adding a block to a vanilla tag (mineable, needs-stone-tool, ...) is done here rather
 * than in hand-written JSON so the mapping is reviewed alongside the block definition.
 * Future systems get their own {@code ultimatelife:*} tags for grouping furniture,
 * storage, devices and so on.
 */
public final class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(final PackOutput output,
                               final CompletableFuture<HolderLookup.Provider> registries,
                               final ExistingFileHelper existingFiles) {
        super(output, registries, UltimateLife.MOD_ID, existingFiles);
    }

    @Override
    protected void addTags(final HolderLookup.Provider registries) {
        // -- Test harness (Phase 1) --------------------------------------
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.ULTIMATE_TEST_BLOCK.get());
    }
}
