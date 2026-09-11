package com.ultimatelife.common.data.provider;

import com.ultimatelife.common.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

/**
 * Generates crafting recipes.
 *
 * <p>Recipes are written in code rather than as JSON files so they are refactored together
 * with the items and blocks they reference.
 */
public final class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(final RecipeOutput output) {
        // -- Test harness (Phase 1) --------------------------------------
        // One shaped recipe so the recipe pipeline is exercised end to end.
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ULTIMATE_TEST_BLOCK.get())
            .define('A', Items.AMETHYST_SHARD)
            .define('C', Items.CRYING_OBSIDIAN)
            .pattern("AAA")
            .pattern("ACA")
            .pattern("AAA")
            .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
            .save(output);
    }
}
