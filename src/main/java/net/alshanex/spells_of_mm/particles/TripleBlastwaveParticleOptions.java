package net.alshanex.spells_of_mm.particles;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import net.alshanex.spells_of_mm.registry.SMMParticleRegistry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TripleBlastwaveParticleOptions implements ParticleOptions {
    private final float scale;
    private final Vector3f color;

    public TripleBlastwaveParticleOptions(Vector3f color, float scale) {
        this.scale = scale;
        this.color = color;
    }

    public float getScale() {
        return this.scale;
    }

    public Vector3f getColor() {
        return this.color;
    }

    public Vector3f color() {
        return color;
    }

    public TripleBlastwaveParticleOptions(float r, float g, float b, float scale) {
        this(new Vector3f(r, g, b), scale);
    }

    private TripleBlastwaveParticleOptions(Vector4f vector4f) {
        this(vector4f.x, vector4f.y, vector4f.z, vector4f.w);
    }

    // Convierte a BlastwaveParticleOptions para usar en las partículas individuales
    public BlastwaveParticleOptions toBlastwaveOptions() {
        return new BlastwaveParticleOptions(color, scale);
    }

    // Para networking
    public static StreamCodec<? super RegistryFriendlyByteBuf, TripleBlastwaveParticleOptions> STREAM_CODEC = StreamCodec.of(
            (buf, option) -> {
                buf.writeFloat(option.color.x);
                buf.writeFloat(option.color.y);
                buf.writeFloat(option.color.z);
                buf.writeFloat(option.scale);
            },
            (buf) -> {
                return new TripleBlastwaveParticleOptions(buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
            }
    );

    // Para comandos
    public static MapCodec<TripleBlastwaveParticleOptions> MAP_CODEC = RecordCodecBuilder.mapCodec(object ->
            object.group(
                    Codec.FLOAT.fieldOf("r").forGetter(p -> ((TripleBlastwaveParticleOptions) p).color.x),
                    Codec.FLOAT.fieldOf("g").forGetter(p -> ((TripleBlastwaveParticleOptions) p).color.y),
                    Codec.FLOAT.fieldOf("b").forGetter(p -> ((TripleBlastwaveParticleOptions) p).color.z),
                    Codec.FLOAT.fieldOf("scale").forGetter(p -> ((TripleBlastwaveParticleOptions) p).scale)
            ).apply(object, TripleBlastwaveParticleOptions::new
            ));

    public @NotNull ParticleType<TripleBlastwaveParticleOptions> getType() {
        return SMMParticleRegistry.TRIPLE_BLASTWAVE_PARTICLE.get();
    }
}
