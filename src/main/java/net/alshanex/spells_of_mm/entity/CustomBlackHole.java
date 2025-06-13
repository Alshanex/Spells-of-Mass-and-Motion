package net.alshanex.spells_of_mm.entity;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.alshanex.spells_of_mm.registry.SMMEntityRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class CustomBlackHole extends Projectile {
    private static final EntityDataAccessor<Float> DATA_RADIUS = SynchedEntityData.defineId(CustomBlackHole.class, EntityDataSerializers.FLOAT);

    public CustomBlackHole(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CustomBlackHole(Level pLevel, LivingEntity owner) {
        this(SMMEntityRegistry.CUSTOM_BLACK_HOLE.get(), pLevel);
        setOwner(owner);
    }

    List<Entity> trackingEntities = new ArrayList<>();

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }


    private int soundTick;
    private float damage;

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(this.getRadius() * 2.0F, this.getRadius() * 2.0F);
    }

    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(DATA_RADIUS, 5F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        if (DATA_RADIUS.equals(pKey)) {
            this.refreshDimensions();
            if (getRadius() < .1f)
                this.discard();
        }

        super.onSyncedDataUpdated(pKey);
    }

    public void setRadius(float pRadius) {
        if (!this.level().isClientSide) {
            this.getEntityData().set(DATA_RADIUS, Math.min(pRadius, 48));
        }
    }

    public float getRadius() {
        return this.getEntityData().get(DATA_RADIUS);
    }

    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putFloat("Radius", this.getRadius());
        pCompound.putInt("Age", this.tickCount);
        pCompound.putFloat("Damage", this.getDamage());

        super.addAdditionalSaveData(pCompound);
    }

    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.tickCount = pCompound.getInt("Age");
        this.damage = pCompound.getFloat("Damage");
        if (damage == 0)
            damage = 1;
        if (pCompound.getInt("Radius") > 0)
            this.setRadius(pCompound.getFloat("Radius"));

        super.readAdditionalSaveData(pCompound);

    }

    @Override
    public void tick() {
        super.tick();
        int update = Math.max((int) (getRadius() / 2), 2);
        //prevent lag from giagantic black holes
        if (tickCount % update == 0) {
            updateTrackingEntities();
        }
        var bb = this.getBoundingBox();
        float radius = (float) (bb.getXsize());
        boolean hitTick = this.tickCount % 10 == 0;
        Vec3 center = bb.getCenter().subtract(0, this.getBbHeight() / 2, 0);
        for (Entity entity : trackingEntities) {
            if (entity != getOwner() && !DamageSources.isFriendlyFireBetween(getOwner(), entity) && !entity.isSpectator()) {
                float distance = (float) center.distanceTo(entity.position());
                if (distance > radius) {
                    continue;
                }
                float f = 1 - distance / radius;
                float scale = f * f * f * f * .25f;
                float resistance = entity instanceof LivingEntity livingEntity ? Mth.clamp(1 - (float) livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), .3f, 1f) : 1f;
                float bossResistance = entity.getType().is(Tags.EntityTypes.BOSSES) ? 0.5f : 1f;


                Vec3 diff = center.subtract(entity.position()).scale(scale * resistance * bossResistance);
                entity.push(diff.x, diff.y, diff.z);
                if (hitTick && distance < 9 && canHitEntity(entity)) {
                    DamageSources.applyDamage(entity, damage, SpellRegistry.BLACK_HOLE_SPELL.get().getDamageSource(this, getOwner()));
                }
                entity.fallDistance = 0;
            }
        }
        if (!level().isClientSide) {
            if (tickCount > 20 * 5) {
                this.discard();
                this.playSound(SoundRegistry.BLACK_HOLE_CAST.get(), getRadius() / 2f, 1);
                MagicManager.spawnParticles(level(), ParticleHelper.UNSTABLE_ENDER, getX(), getY() + getRadius(), getZ(), 200, 1, 1, 1, 1, true);
                for (Entity entity : trackingEntities) {
                    if (entity.distanceToSqr(center) < 9) {
                        entity.setDeltaMovement(entity.getDeltaMovement().add(entity.position().subtract(center).normalize().scale(0.5f)));
                        entity.hurtMarked = true;
                    }
                }
            }
        }
    }

    private void updateTrackingEntities() {
        trackingEntities = level().getEntities(this, this.getBoundingBox().inflate(1));
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}
