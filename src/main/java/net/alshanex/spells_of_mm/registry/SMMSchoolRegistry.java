package net.alshanex.spells_of_mm.registry;

import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.alshanex.spells_of_mm.SpellsOfMM;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class SMMSchoolRegistry {
    public static final ResourceLocation GRAVITY_RESOURCE = ResourceLocation.fromNamespaceAndPath(SpellsOfMM.MODID, "gravity");
    public static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, SpellsOfMM.MODID);

    public static final TagKey<Item> GRAVITY_FOCUS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(SpellsOfMM.MODID, "gravity_focus"));
    public static final ResourceKey<DamageType> GRAVITY_MAGIC = ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(SpellsOfMM.MODID, "gravity_magic"));

    public static final Supplier<SchoolType> GRAVITY = registerSchool(new SchoolType(
            GRAVITY_RESOURCE,
            GRAVITY_FOCUS,
            Component.translatable("school.spells_of_mm.gravity").withColor(0x6E83AA),
            SMMAttributeRegistry.GRAVITY_SPELL_POWER,
            SMMAttributeRegistry.GRAVITY_MAGIC_RESIST,
            SoundRegistry.BLACK_HOLE_CAST,
            GRAVITY_MAGIC
    ));

    private static Supplier<SchoolType> registerSchool(SchoolType schoolType) {
        return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
    }
}
