package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Creative mode tabs of the mod.
 *
 * <p>Furniture, backpack, storage, NPC, vehicle and device contents will be added to the
 * {@link #ULTIMATE_LIFE} tab by calling
 * {@code event.accept(...)} on the tab builder below, or by listening to
 * {@link net.minecraftforge.event.BuildCreativeModeTabContentsEvent} for cross-mod tabs.</p>
 */
public final class ModCreativeTabs {

    private ModCreativeTabs() {
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, UltimateLife.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ULTIMATE_LIFE = CREATIVE_MODE_TABS.register(
            "ultimate_life",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + UltimateLife.MOD_ID))
                    .icon(() -> new ItemStack(ModItems.ULTIMATE_TEST_BLOCK_ITEM.get()))
                    .displayItems((parameters, output) -> output.accept(ModItems.ULTIMATE_TEST_BLOCK_ITEM.get()))
                    .build());
}
