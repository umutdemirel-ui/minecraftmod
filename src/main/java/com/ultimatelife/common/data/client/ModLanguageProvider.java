package com.ultimatelife.common.data.client;

import com.ultimatelife.UltimateLife;
import com.ultimatelife.common.registry.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * Generates {@code assets/ultimatelife/lang/en_us.json}.
 *
 * <p>Adding a block, item, tab or key binding means adding one line here - the language file is never
 * edited by hand.</p>
 */
public final class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(final PackOutput output) {
        super(output, UltimateLife.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ModBlocks.ULTIMATE_TEST_BLOCK.get(), "Ultimate Test Block");
        add("itemGroup." + UltimateLife.MOD_ID, UltimateLife.MOD_NAME);
    }
}
