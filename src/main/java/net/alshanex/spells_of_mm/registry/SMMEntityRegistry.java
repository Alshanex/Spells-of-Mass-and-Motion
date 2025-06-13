package net.alshanex.spells_of_mm.registry;

import net.alshanex.spells_of_mm.SpellsOfMM;
import net.alshanex.spells_of_mm.entity.CustomBlackHole;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SMMEntityRegistry {
    private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, SpellsOfMM.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static final DeferredHolder<EntityType<?>, EntityType<CustomBlackHole>> CUSTOM_BLACK_HOLE =
            ENTITIES.register("custom_black_hole", () -> EntityType.Builder.<CustomBlackHole>of(CustomBlackHole::new, MobCategory.MISC)
                    .sized(11, 11)
                    .clientTrackingRange(64)
                    .build(ResourceLocation.fromNamespaceAndPath(SpellsOfMM.MODID, "custom_black_hole").toString()));

}
