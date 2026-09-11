package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * The single {@code Ultimate Life} creative tab.
 *
 * <p>The tab itself is registered here, but its <em>contents</em> are filled from
 * {@link com.ultimatelife.common.event.ModCommonEvents} through
 * {@code BuildCreativeModeTabContentsEvent}. Splitting the two keeps this class free of
 * item dependencies, so adding furniture/backpack/vehicle entries later never touches the
 * registration path.
 */
public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltimateLife.MOD_ID);

    private ModCreativeTabs() {
    }

    public static final RegistryObject<CreativeModeTab> ULTIMATE_LIFE_TAB = CREATIVE_TABS.register(
        "ultimate_life",
        () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + UltimateLife.MOD_ID))
            .icon(() -> ModItems.ULTIMATE_TEST_BLOCK_ITEM.get().getDefaultInstance())
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .build()
    );
}
