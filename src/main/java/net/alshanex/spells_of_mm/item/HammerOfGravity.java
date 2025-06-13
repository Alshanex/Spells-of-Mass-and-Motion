package net.alshanex.spells_of_mm.item;

import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.alshanex.spells_of_mm.registry.SMMSpellRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;

public class HammerOfGravity extends SwordItem implements IPresetSpellContainer {
    public HammerOfGravity(Tier tier, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, 12, -3.4f)));
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack == null) {
            return;
        }

        if (!ISpellContainer.isSpellContainer(itemStack)) {
            var spellContainer = ISpellContainer.create(1, true, false).mutableCopy();
            spellContainer.addSpell(SMMSpellRegistry.FRONT_SMASH.get(), 5, true);
            itemStack.set(ComponentRegistry.SPELL_CONTAINER, spellContainer.toImmutable());
        }
    }
}
