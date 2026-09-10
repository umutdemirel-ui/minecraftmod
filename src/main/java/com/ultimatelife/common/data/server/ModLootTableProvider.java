package com.ultimatelife.common.data.server;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Generates {@code data/ultimatelife/loot_tables/**}.
 *
 * <p>Only block loot is generated today; chest, entity and gameplay loot sub providers are added here
 * as the systems that need them land.</p>
 */
public final class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(),
                List.of(new SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK)),
                lookupProvider);
    }
}
