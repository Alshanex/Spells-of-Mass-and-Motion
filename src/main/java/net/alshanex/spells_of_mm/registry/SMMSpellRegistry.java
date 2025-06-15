package net.alshanex.spells_of_mm.registry;

import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import net.alshanex.spells_of_mm.SpellsOfMM;
import net.alshanex.spells_of_mm.spells.FrontSmashSpell;
import net.alshanex.spells_of_mm.spells.GravityShift;
import net.alshanex.spells_of_mm.spells.GroundSmashSpell;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.redspace.ironsspellbooks.api.registry.SpellRegistry.SPELL_REGISTRY_KEY;

public class SMMSpellRegistry {
    public static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SPELL_REGISTRY_KEY, SpellsOfMM.MODID);
    public static void register(IEventBus eventBus) {
        SPELLS.register(eventBus);
    }

    private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
        return SPELLS.register(spell.getSpellName(), () -> spell);
    }

    public static final Supplier<AbstractSpell> FRONT_SMASH = registerSpell(new FrontSmashSpell());
    public static final Supplier<AbstractSpell> GROUND_SMASH = registerSpell(new GroundSmashSpell());
    public static final Supplier<AbstractSpell> GRAVITY_SHIFT = registerSpell(new GravityShift());
}
