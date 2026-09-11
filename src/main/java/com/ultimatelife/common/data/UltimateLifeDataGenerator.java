package com.ultimatelife.common.data;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.data.provider.ModAdvancementProvider;
import com.ultimatelife.common.data.provider.ModBlockLootProvider;
import com.ultimatelife.common.data.provider.ModBlockStateProvider;
import com.ultimatelife.common.data.provider.ModBlockTagProvider;
import com.ultimatelife.common.data.provider.ModRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.CompletableFuture;

/**
 * Data generation wiring.
 *
 * <p>Everything the mod ships that can be derived from code - blockstates, models, loot
 * tables, recipes, tags, advancements - is produced here and written to
 * {@code src/generated/resources}, which {@code build.gradle} adds to the resource
 * source set. Hand-written assets (lang files, textures) stay in
 * {@code src/main/resources} and are passed to the generator through
 * {@link ExistingFileHelper} so generated files can reference them.
 *
 * <p>Run with {@code ./gradlew runData}.
 */
@Mod.EventBusSubscriber(modid = UltimateLife.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class UltimateLifeDataGenerator {

    private UltimateLifeDataGenerator() {
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput output = generator.getPackOutput();
        final ExistingFileHelper existingFiles = event.getExistingFileHelper();
        final CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        // Client-facing assets: blockstates and models.
        if (event.includeClient()) {
            generator.addProvider(true, new ModBlockStateProvider(output, existingFiles));
        }

        // Server-facing data: loot, recipes, tags, advancements.
        if (event.includeServer()) {
            generator.addProvider(true, new ModBlockLootProvider(output, registries));
            generator.addProvider(true, new ModRecipeProvider(output, registries));
            generator.addProvider(true, new ModBlockTagProvider(output, registries, existingFiles));
            generator.addProvider(true, new ModAdvancementProvider(output, registries, existingFiles));
        }
    }
}
