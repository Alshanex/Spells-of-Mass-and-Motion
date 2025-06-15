package net.alshanex.spells_of_mm.registry;

import com.mojang.serialization.MapCodec;
import net.alshanex.spells_of_mm.SpellsOfMM;
import net.alshanex.spells_of_mm.particles.TripleBlastwaveParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SMMParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(Registries.PARTICLE_TYPE, SpellsOfMM.MODID);

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }

    public static final Supplier<SimpleParticleType> VIBRATION_PARTICLE = PARTICLE_TYPES.register("vibration", () -> new SimpleParticleType(false));

    public static final Supplier<ParticleType<TripleBlastwaveParticleOptions>> TRIPLE_BLASTWAVE_PARTICLE = PARTICLE_TYPES.register("triple_blastwave", () -> new ParticleType<>(true) {
        public MapCodec<TripleBlastwaveParticleOptions> codec() {
            return TripleBlastwaveParticleOptions.MAP_CODEC;
        }
        public StreamCodec<? super RegistryFriendlyByteBuf, TripleBlastwaveParticleOptions> streamCodec() {
            return TripleBlastwaveParticleOptions.STREAM_CODEC;
        }
    });
}
