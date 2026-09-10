package com.ultimatelife.common.data.server;

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
 * Generates {@code data/ultimatelife/recipes/*.json}.
 */
public final class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(final RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ULTIMATE_TEST_BLOCK.get(), 1)
                .pattern("SSS")
                .pattern("SIS")
                .pattern("SSS")
                .define('S', Items.IRON_INGOT)
                .define('I', Items.DIAMOND)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);
    }
}
