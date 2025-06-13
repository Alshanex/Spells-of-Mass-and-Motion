package net.alshanex.spells_of_mm.event;

import net.alshanex.spells_of_mm.SpellsOfMM;
import net.alshanex.spells_of_mm.registry.SMMAttributeRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = SpellsOfMM.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent e) {
        e.getTypes().forEach(entity -> SMMAttributeRegistry.ATTRIBUTES.getEntries().forEach(attribute -> e.add(entity, attribute)));
    }
}
