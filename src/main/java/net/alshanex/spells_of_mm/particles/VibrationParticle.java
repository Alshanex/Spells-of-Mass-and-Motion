package net.alshanex.spells_of_mm.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class VibrationParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    public VibrationParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {

        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        //this.friction = 1.0f;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize *= 2f;
        this.scale(2f);

        this.lifetime = 5 + (int) (Math.random() * 5);

        sprites = spriteSet;
        this.gravity = 0F;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    public void tick() {
        super.tick();

        this.setSpriteFromAge(this.sprites);

        float lifeProgress = (float) this.age / (float) this.lifetime;

        // Cambio de tamaño más pronunciado y rápido
        this.quadSize = 1f + lifeProgress * 3f; // Reducido de 5f a 3f para menos crecimiento

        // Opcional: Fade out más rápido hacia el final
        if (lifeProgress > 0.7f) {
            float fadeProgress = (lifeProgress - 0.7f) / 0.3f;
            this.alpha = 1.0f - fadeProgress;
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT; // Cambiado para soportar transparencia
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            return new VibrationParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return LightTexture.FULL_BRIGHT;
    }
}