package com.ultimatelife.common.data.provider;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Generates advancements.
 *
 * <p>One root advancement per mod is the vanilla convention: every later advancement hangs
 * off it, so each future system only has to add its own branch.
 */
public final class ModAdvancementProvider extends ForgeAdvancementProvider {

    public ModAdvancementProvider(final PackOutput output,
                                  final CompletableFuture<HolderLookup.Provider> registries,
                                  final ExistingFileHelper existingFiles) {
        super(output, registries, existingFiles, List.of(new RootAdvancement()));
    }

    private static final class RootAdvancement implements ForgeAdvancementProvider.AdvancementGenerator {

        @Override
        public void generate(final HolderLookup.Provider registries,
                             final Consumer<AdvancementHolder> consumer,
                             final ExistingFileHelper existingFiles) {
            Advancement.Builder.advancement()
                .display(
                    ModItems.ULTIMATE_TEST_BLOCK_ITEM.get(),
                    Component.translatable("advancements." + UltimateLife.MOD_ID + ".root.title"),
                    Component.translatable("advancements." + UltimateLife.MOD_ID + ".root.description"),
                    ResourceLocation.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png"),
                    AdvancementType.TASK,
                    true,
                    true,
                    false)
                .addCriterion("has_test_block",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.ULTIMATE_TEST_BLOCK_ITEM.get()))
                .save(consumer, ResourceLocation.fromNamespaceAndPath(UltimateLife.MOD_ID, "root"));
        }
    }
}
