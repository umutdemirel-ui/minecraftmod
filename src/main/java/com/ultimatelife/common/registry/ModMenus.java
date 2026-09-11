package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Container/menu types owned by Ultimate Life (backpack, storage, mail...).
 *
 * <p>Empty in Phase 1. Menus are server-authoritative: the {@link MenuType} is created with
 * an {@code IContainerFactory} that reads the extra data Forge appends to the open-screen
 * packet, and the matching {@code MenuScreen} lives in {@code com.ultimatelife.client.screen}.
 */
public final class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
        DeferredRegister.create(ForgeRegistries.MENU_TYPES, UltimateLife.MOD_ID);

    private ModMenus() {
    }
}
