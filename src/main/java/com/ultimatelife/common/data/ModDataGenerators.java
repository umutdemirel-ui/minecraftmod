package com.ultimatelife.common.data;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.data.client.ModBlockStateProvider;
import com.ultimatelife.common.data.client.ModLanguageProvider;
import com.ultimatelife.common.data.server.ModBlockTagsProvider;
import com.ultimatelife.common.data.server.ModLootTableProvider;
import com.ultimatelife.common.data.server.ModRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

/**
 * Data generation entry point.
 *
 * <p>Everything that can be derived from code is generated instead of being hand written: block
 * states, block/item models, language files, recipes, loot tables and tags. Run
 * {@code ./gradlew runData} to refresh {@code src/generated/resources}.</p>
 *
 * <p>Providers that emit client resources live under {@code com.ultimatelife.common.data.client} and
 * are only instantiated when the client data set is being generated, which keeps the split with the
 * server side providers explicit.</p>
 */
@Mod.EventBusSubscriber(modid = UltimateLife.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModDataGenerators {

    private ModDataGenerators() {
    }

    @SubscribeEvent
    static void onGatherData(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        final PackOutput packOutput = generator.getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        final ExistingFileHelper existingFiles = event.getExistingFileHelper();

        if (event.includeClient()) {
            generator.addProvider(true, new ModBlockStateProvider(packOutput, existingFiles));
            generator.addProvider(true, new ModLanguageProvider(packOutput));
        }

        if (event.includeServer()) {
            generator.addProvider(true, new ModRecipeProvider(packOutput, lookupProvider));
            generator.addProvider(true, new ModLootTableProvider(packOutput, lookupProvider));
            generator.addProvider(true, new ModBlockTagsProvider(packOutput, lookupProvider, existingFiles));
        }
    }
}
