package com.ultimatelife.common.data.client;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Generates block states and block/item models.
 *
 * <p>Assets that belong to the client are generated here; the block/item definitions themselves stay
 * in {@link com.ultimatelife.common.registry.ModBlocks} and
 * {@link com.ultimatelife.common.registry.ModItems} so the server and the data generator share one
 * source of truth.</p>
 */
public final class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(final PackOutput output, final ExistingFileHelper existingFiles) {
        super(output, UltimateLife.MOD_ID, existingFiles);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.ULTIMATE_TEST_BLOCK.get(), testBlockModel());
    }

    /**
     * The test block renders with transparency in its frame, so it uses the cutout render type. This is
     * also the pipeline future furniture blocks will use.
     */
    private BlockModelBuilder testBlockModel() {
        return models()
                .cubeAll("ultimate_test_block", modLoc("block/ultimate_test_block"))
                .renderType("cutout_mipped");
    }
}
