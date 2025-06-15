package net.alshanex.spells_of_mm.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.alshanex.spells_of_mm.SpellsOfMM;
import net.alshanex.spells_of_mm.particles.TripleBlastwaveParticleOptions;
import net.alshanex.spells_of_mm.registry.SMMParticleRegistry;
import net.alshanex.spells_of_mm.registry.SMMSchoolRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

@AutoSpellConfig
public class GravityShift extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SpellsOfMM.MODID, "gravity_shift");

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)),
                Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(effectDuration(spellLevel), 1)));
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.COMMON)
            .setSchoolResource(SMMSchoolRegistry.GRAVITY_RESOURCE)
            .setMaxLevel(10)
            .setCooldownSeconds(3)
            .build();

    public GravityShift() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 10;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, .5f, false);
        return true;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 spawn = null;
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            spawn = castTargetingData.getTargetPosition((ServerLevel) level);
            LivingEntity target = castTargetingData.getTarget((ServerLevel) level);
            if(target != null && spawn != null){
                target.addDeltaMovement(new Vec3(0, 1, 0));
                target.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, effectDuration(spellLevel),1, false, false, true));
                MagicManager.spawnParticles(level, new TripleBlastwaveParticleOptions(SMMSchoolRegistry.GRAVITY.get().getTargetingColor(), 1), spawn.x, spawn.y + .33f, spawn.z, 1, 0, 0, 0, 0, true);
                DamageSources.applyDamage(target, getDamage(spellLevel, entity), getDamageSource(entity));
            }
        }
        if (spawn == null) {
            spawn = entity.position();
            entity.setDeltaMovement(entity.getDeltaMovement().add(0, 1, 0));
            entity.hurtMarked = true;
            entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, effectDuration(spellLevel),1, false, false, true));
            MagicManager.spawnParticles(level, new TripleBlastwaveParticleOptions(SMMSchoolRegistry.GRAVITY.get().getTargetingColor(), 1), spawn.x, spawn.y + .33f, spawn.z, 1, 0, 0, 0, 0, true);
        }

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) * .5f;
    }

    private int effectDuration(int spellLevel){
        return spellLevel * 20;
    }
}
