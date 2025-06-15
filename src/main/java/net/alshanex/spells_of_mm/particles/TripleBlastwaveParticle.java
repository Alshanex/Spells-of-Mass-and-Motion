package net.alshanex.spells_of_mm.particles;

import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleOptions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class TripleBlastwaveParticle extends NoRenderParticle {
    private final TripleBlastwaveParticleOptions options;
    private final SpriteSet sprites;
    private boolean firstSpawned = false;
    private boolean secondSpawned = false;
    private boolean thirdSpawned = false;

    // Timing en ticks (20 ticks = 1 segundo, 5 ticks = 1/4 segundo)
    private static final int SPAWN_DELAY = 5;

    public TripleBlastwaveParticle(ClientLevel level, double x, double y, double z,
                                   SpriteSet sprites, TripleBlastwaveParticleOptions options) {
        super(level, x, y, z, 0, 0, 0);
        this.sprites = sprites;
        this.options = options;
        this.lifetime = SPAWN_DELAY * 2 + 1; // Suficiente para spawnar las 3 partículas
    }

    @Override
    public void tick() {
        // Spawnar primera partícula (la más baja) inmediatamente
        if (!firstSpawned && age == 0) {
            spawnBlastwaveAt(y - 0.333); // 1/3 de bloque más abajo
            firstSpawned = true;
        }

        // Spawnar segunda partícula después de 1/4 segundo
        if (!secondSpawned && age == SPAWN_DELAY) {
            spawnBlastwaveAt(y); // Altura original
            secondSpawned = true;
        }

        // Spawnar tercera partícula después de otro 1/4 segundo
        if (!thirdSpawned && age == SPAWN_DELAY * 2) {
            spawnBlastwaveAt(y + 0.333); // 1/3 de bloque más arriba
            thirdSpawned = true;
        }

        // Aumentar edad y remover cuando hayamos spawneado todas las partículas
        if (age++ >= lifetime) {
            remove();
        }
    }

    private void spawnBlastwaveAt(double height) {
        // Convertir a BlastwaveParticleOptions
        BlastwaveParticleOptions blastwaveOptions = options.toBlastwaveOptions();

        // Spawnar la partícula usando el sistema de partículas de Minecraft
        level.addParticle(blastwaveOptions, x, height, z, 0, 0, 0);
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<TripleBlastwaveParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(@NotNull TripleBlastwaveParticleOptions options,
                                       @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new TripleBlastwaveParticle(level, x, y, z, sprites, options);
        }
    }
}