package com.ultimatelife.common.registry;

import com.ultimatelife.UltimateLife;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.DeferredRegister;

/**
 * Particle type registry.
 *
 * <p>Empty in Phase 1. Particle types are registered here (common side); their factories live in
 * {@code com.ultimatelife.client.particle} so a dedicated server never loads rendering code.</p>
 */
public final class ModParticles {

    private ModParticles() {
    }

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, UltimateLife.MOD_ID);
}
