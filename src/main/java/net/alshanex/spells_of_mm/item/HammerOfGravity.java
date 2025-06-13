package net.alshanex.spells_of_mm.item;

import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;

public class HammerOfGravity extends SwordItem {
    public HammerOfGravity(Tier tier, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, 12, -3.4f)));
    }
}
