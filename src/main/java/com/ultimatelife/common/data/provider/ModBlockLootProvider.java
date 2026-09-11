package com.ultimatelife.common.data.provider;

import com.ultimatelife.common.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Generates block loot tables.
 *
 * <p>{@link #getKnownBlocks()} is restricted to blocks registered by this mod, so the
 * provider never re-emits vanilla tables into our namespace.
 */
public final class ModBlockLootProvider extends LootTableProvider {

    public ModBlockLootProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK)), registries);
    }

    private static final class ModBlockLoot extends BlockLootSubProvider {

        private ModBlockLoot(final HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            // -- Test harness (Phase 1) ----------------------------------
            dropSelf(ModBlocks.ULTIMATE_TEST_BLOCK.get());

            // Future systems add their tables here; anything that needs silk-touch,
            // fortune or a container drop gets an explicit table instead of dropSelf.
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).toList();
        }
    }
}
