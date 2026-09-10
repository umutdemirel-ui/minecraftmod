package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Menu (container) type registry.
 *
 * <p>Empty in Phase 1. Menus declared here are opened from the server side only; their screens live
 * in {@code com.ultimatelife.client.screen} and are registered from the client bootstrap.</p>
 */
public final class ModMenus {

    private ModMenus() {
    }

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, UltimateLife.MOD_ID);
}
