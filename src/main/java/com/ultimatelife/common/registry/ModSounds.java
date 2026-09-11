package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Sound events owned by Ultimate Life.
 *
 * <p>Empty in Phase 1. Audio files go to {@code assets/ultimatelife/sounds} and are
 * described by {@code sounds.json}; {@code SoundEvent} is only the registry handle and is
 * therefore safe on both physical sides.
 */
public final class ModSounds {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, UltimateLife.MOD_ID);

    private ModSounds() {
    }
}
