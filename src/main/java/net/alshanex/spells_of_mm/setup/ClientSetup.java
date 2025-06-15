package net.alshanex.spells_of_mm.setup;

import net.alshanex.spells_of_mm.SpellsOfMM;
import net.alshanex.spells_of_mm.particles.VibrationParticle;
import net.alshanex.spells_of_mm.registry.SMMEntityRegistry;
import net.alshanex.spells_of_mm.registry.SMMParticleRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = SpellsOfMM.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SMMEntityRegistry.CUSTOM_BLACK_HOLE.get(), NoopRenderer::new);
    }

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SMMParticleRegistry.VIBRATION_PARTICLE.get(), VibrationParticle.Provider::new);
    }
}
