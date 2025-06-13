package net.alshanex.spells_of_mm.util;

import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.alshanex.spells_of_mm.SpellsOfMM;
import net.minecraft.resources.ResourceLocation;

public class SMMAnimations {
    public static ResourceLocation ANIMATION_RESOURCE = ResourceLocation.fromNamespaceAndPath(SpellsOfMM.MODID, "animation");

    public static final AnimationHolder HAMMER_CHARGE = new AnimationHolder("spells_of_mm:hammer_charge", true, true);
    public static final AnimationHolder HAMMER_SMASH_FRONT = new AnimationHolder("spells_of_mm:hammer_smash_front", true, true);
    public static final AnimationHolder HAMMER_SMASH_GROUND = new AnimationHolder("spells_of_mm:hammer_smash_ground", true, true);
}
