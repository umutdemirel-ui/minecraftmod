package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Block registry.
 *
 * <p>Future systems (furniture, storage, vehicles...) register their blocks here through their own
 * {@link DeferredRegister} or by adding entries to {@link #BLOCKS}; nothing else in the mod needs to
 * know how a block was created.</p>
 */
public final class ModBlocks {

    private ModBlocks() {
    }

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, UltimateLife.MOD_ID);

    /**
     * Phase 1 smoke-test block. It exists to prove the whole chain works end to end:
     * registry -&gt; blockstate/model generation -&gt; texture -&gt; loot table -&gt; creative tab.
     */
    public static final RegistryObject<Block> ULTIMATE_TEST_BLOCK = BLOCKS.register(
            "ultimate_test_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.METAL)
                    // The generated model renders with "cutout_mipped", see ModBlockStateProvider.
                    .noOcclusion()));
}
