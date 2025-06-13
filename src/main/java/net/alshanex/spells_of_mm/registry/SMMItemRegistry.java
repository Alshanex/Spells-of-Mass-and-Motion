package net.alshanex.spells_of_mm.registry;

import net.alshanex.spells_of_mm.SpellsOfMM;
import net.alshanex.spells_of_mm.item.HammerOfGravity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SMMItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SpellsOfMM.MODID);

    public static final DeferredItem<HammerOfGravity> HAMMER_OF_GRAVITY = ITEMS.register("hammer_of_gravity",
            () -> new HammerOfGravity(
                    Tiers.NETHERITE,
                    new Item.Properties()
                            .rarity(Rarity.EPIC)
                            .stacksTo(1)
                            .durability(2000)
            )
    );
}
