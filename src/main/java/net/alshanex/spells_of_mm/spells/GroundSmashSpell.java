package net.alshanex.spells_of_mm.spells;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.EarthquakeAoe;
import io.redspace.ironsspellbooks.entity.spells.black_hole.BlackHole;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.alshanex.spells_of_mm.SpellsOfMM;
import net.alshanex.spells_of_mm.entity.CustomBlackHole;
import net.alshanex.spells_of_mm.particles.TripleBlastwaveParticleOptions;
import net.alshanex.spells_of_mm.registry.SMMItemRegistry;
import net.alshanex.spells_of_mm.registry.SMMSchoolRegistry;
import net.alshanex.spells_of_mm.util.SMMAnimations;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@AutoSpellConfig
public class GroundSmashSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(SpellsOfMM.MODID, "ground_smash");
    private CustomBlackHole blackhole = null;

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", getDamageText(spellLevel, caster)));
    }

    private final DefaultConfig defaultConfig = new DefaultConfig()
            .setMinRarity(SpellRarity.RARE)
            .setSchoolResource(SMMSchoolRegistry.GRAVITY_RESOURCE)
            .setMaxLevel(5)
            .setCooldownSeconds(20)
            .build();

    public GroundSmashSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 3;
        this.castTime = 40;
        this.baseManaCost = 30;
    }

    @Override
    public boolean canBeInterrupted(Player player) {
        return false;
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        //due to melee animation timing, we do not want cast time attribute to affect this spell
        return getCastTime(spellLevel);
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.DIVINE_SMITE_WINDUP.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.DIVINE_SMITE_CAST.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if(!entity.getMainHandItem().is(SMMItemRegistry.HAMMER_OF_GRAVITY.get())){ return false;}
        return super.checkPreCastConditions(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @org.jetbrains.annotations.Nullable MagicData playerMagicData) {
        this.blackhole = new CustomBlackHole(level, entity);
        this.blackhole.setRadius(5);
        this.blackhole.setDamage(0);
        this.blackhole.moveTo(entity.position().subtract(0, entity.getBbHeight(), 0));
        level.addFreshEntity(blackhole);

        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 8.2f;
        Vec3 particleLocation = level.clip(new ClipContext(entity.position(), entity.position().add(0, -2, 0), ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty())).getLocation().add(0, 0.1, 0);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SMMSchoolRegistry.GRAVITY.get().getTargetingColor(), radius),
                particleLocation.x, particleLocation.y, particleLocation.z, 1, 0, 0, 0, 0, true);
        MagicManager.spawnParticles(level, ParticleHelper.UNSTABLE_ENDER, particleLocation.x, particleLocation.y, particleLocation.z, 50, 0, 0, 0, 1, false);
        CameraShakeManager.addCameraShake(new CameraShakeData(10, particleLocation, 10));

        var entities = level.getEntities(entity, AABB.ofSize(entity.position(), radius, radius, radius));
        var damageSource = this.getDamageSource(entity);
        for (Entity targetEntity : entities) {
            //double distance = targetEntity.distanceToSqr(smiteLocation);
            if (targetEntity.isAlive() && targetEntity.isPickable()) {
                if (DamageSources.applyDamage(targetEntity, getDamage(spellLevel, entity), damageSource)) {
                    EnchantmentHelper.doPostAttackEffects((ServerLevel) level, targetEntity, damageSource);
                    if(targetEntity instanceof LivingEntity livingEntity){
                        livingEntity.addDeltaMovement(new Vec3(0, 1, 0));
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 60,1, false, false, true));
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 120,1, false, false, true));
                        MagicManager.spawnParticles(level, new TripleBlastwaveParticleOptions(SMMSchoolRegistry.GRAVITY.get().getTargetingColor(), 1), livingEntity.getX(), livingEntity.getY() + .33f, livingEntity.getZ(), 1, 0, 0, 0, 0, true);
                    }
                }
            }
        }

        EarthquakeAoe aoeEntity = new EarthquakeAoe(level);
        aoeEntity.moveTo(entity.position());
        aoeEntity.setOwner(entity);
        aoeEntity.setCircular();
        aoeEntity.setRadius(radius);
        aoeEntity.setDuration(100);
        aoeEntity.setDamage(getDamage(spellLevel, entity) / 10);
        aoeEntity.setSlownessAmplifier(getSlownessAmplifier(spellLevel, entity));
        level.addFreshEntity(aoeEntity);

        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastComplete(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData, boolean cancelled) {
        this.blackhole.remove(Entity.RemovalReason.DISCARDED);
        this.blackhole = null;

        super.onServerCastComplete(level, spellLevel, entity, playerMagicData, cancelled);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return getSpellPower(spellLevel, entity) + getAdditionalDamage(entity);
    }

    private int getSlownessAmplifier(int spellLevel, LivingEntity caster) {
        return Math.max(0, (int) getDamage(spellLevel, caster) - 2);
    }

    private float getAdditionalDamage(LivingEntity entity) {
        if (entity == null) {
            return 0;
        }
        float weaponDamage = Utils.getWeaponDamage(entity);
        var weaponItem = entity.getWeaponItem();
        if (!weaponItem.isEmpty() && weaponItem.has(DataComponents.ENCHANTMENTS)) {
            weaponDamage += Utils.processEnchantment(entity.level(), Enchantments.SMITE, EnchantmentEffectComponents.DAMAGE, weaponItem.get(DataComponents.ENCHANTMENTS));
        }
        return weaponDamage;
    }


    private String getDamageText(int spellLevel, LivingEntity entity) {
        if (entity != null) {
            float weaponDamage = getAdditionalDamage(entity);
            String plus = "";
            if (weaponDamage > 0) {
                plus = String.format(" (+%s)", Utils.stringTruncation(weaponDamage, 1));
            }
            String damage = Utils.stringTruncation(getDamage(spellLevel, entity), 1);
            return damage + plus;
        }
        return "" + getSpellPower(spellLevel, entity);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SMMAnimations.HAMMER_CHARGE;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SMMAnimations.HAMMER_SMASH_GROUND;
    }
}
