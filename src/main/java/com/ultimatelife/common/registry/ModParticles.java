package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Particle types owned by Ultimate Life.
 *
 * <p>Empty in Phase 1. Only the {@link ParticleType} (the synced, serialisable handle) is
 * registered here; the client-side {@code ParticleProvider} that turns it into something
 * visible is registered from {@code com.ultimatelife.client.particle} on the client only.
 */
public final class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, UltimateLife.MOD_ID);

    private ModParticles() {
    }
}
