package net.alshanex.spells_of_mm.registry;

import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import net.alshanex.spells_of_mm.SpellsOfMM;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SMMAttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, SpellsOfMM.MODID);

    public static final DeferredHolder<Attribute, Attribute> GRAVITY_MAGIC_RESIST = newResistanceAttribute("gravity");
    public static final DeferredHolder<Attribute, Attribute> GRAVITY_SPELL_POWER = newPowerAttribute("gravity");

    private static DeferredHolder<Attribute, Attribute> newResistanceAttribute(String id) {
        return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.spells_of_mm." + id + "_magic_resist", 1.0D, -100, 100).setSyncable(true)));
    }

    private static DeferredHolder<Attribute, Attribute> newPowerAttribute(String id) {
        return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.spells_of_mm." + id + "_spell_power", 1.0D, -100, 100).setSyncable(true)));
    }
}
