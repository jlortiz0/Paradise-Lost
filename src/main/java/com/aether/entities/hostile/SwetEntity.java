package com.aether.entities.hostile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SwetEntity extends SlimeEntity {

    protected int initialSize = 2;
    protected static final EntityAttributeModifier knockbackResistanceModifier = new EntityAttributeModifier(
            "Temporary swet knockback resistance",
            1,
            EntityAttributeModifier.Operation.ADDITION);

    public SwetEntity(World world) {
        this(AetherEntityTypes.WHITE_SWET, world);
    }

    public SwetEntity(EntityType<? extends SwetEntity> entityType, World world) {
        super(entityType, world);
        init();
    }

    @Override
    protected void initGoals() {
        // Replace the inherited slime target selectors with one that avoids chasing absorbed players, and ignores iron golems
        super.initGoals();
        this.targetSelector.clear();
        this.targetSelector.add(1, new FollowUnabsorbedTargetGoal<>(
                this, PlayerEntity.class, 10, true, false, (player) ->
                Math.abs(player.getY() - this.getY()) <= 4.0D &&
                        !(canAbsorb(this, player))
        ));
    }

    protected void init() {
        getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(25);
        setHealth(getMaxHealth());
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
    }

    public static DefaultAttributeContainer.Builder initAttributes() {
        return AetherEntityTypes.getDefaultAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28000000417232513D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.25D)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 25.0D);
    }

    @Override
    public void tick() {
        // Entities don't have onEntityCollision, so this does that
        if (!this.isDead()) { // TODO: maybe also don't pick up entities in easy mode?
            world.getOtherEntities(this, this.getBoundingBox()).forEach(this::onEntityCollision);
        }
        super.tick();
    }

    @Override
    public void onPlayerCollision(PlayerEntity player) {
        // Already taken care of in tick()
    }

    protected void onEntityCollision(Entity entity){
        if (!(entity instanceof SwetEntity swet)) {
            if (isAbsorbable(entity)) {
                // The higher the number this is multiplied by, the stiffer the wobble is
                // If the wobbles feel too sharp, try changing the clamp below
                // TODO: Make sure this works in multiplayer
                Vec3d suckVelocity = this.getBoundingBox().getCenter().subtract(entity.getPos()).multiply(0.55);
                Vec3d newVelocity = entity.getVelocity().add(suckVelocity);
                double velocityClamp = this.getSize() * 0.1 + 0.25;
                entity.setVelocity(MathHelper.clamp(newVelocity.getX(), -velocityClamp, velocityClamp),
                        Math.min(newVelocity.getY(), 0.25),
                        MathHelper.clamp(newVelocity.getZ(), -velocityClamp, velocityClamp));
                entity.velocityDirty = true;
                entity.fallDistance = 0;
            }

            // Make items ride the swet. They often shake free with the jiggle physics
            if (entity instanceof ItemEntity) {
                entity.startRiding(this, true);
            }

            if (entity instanceof LivingEntity livingEntity) {
                // Hack to prevent knockback; TODO: find a better way to prevent knockback
                EntityAttributeInstance knockbackResistance = livingEntity.getAttributeInstance(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
                if (knockbackResistance != null) {
                    knockbackResistance.addTemporaryModifier(knockbackResistanceModifier);
                    this.damage(livingEntity);
                    knockbackResistance.removeModifier(knockbackResistanceModifier);
                } else {
                    this.damage(livingEntity);
                }
            }
        } else if (this.getSize() >= swet.getSize() && !swet.isDead()) {
            this.setSize(MathHelper.ceil(MathHelper.sqrt(this.getSize()*this.getSize() + swet.getSize()*swet.getSize())), true);
            swet.discard();
        }
    }

    // Prevent pushing entities away, as this interferes with sucking them in
    @Override
    public boolean isPushable() {
        return false;
    }

    // Same as above
    @Override
    protected void pushAway(Entity entity) {
    }

    @Override
    public void setSize(int size, boolean heal){
        super.setSize(size, heal);
    }

    @Override
    @Nullable
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt){
        setSize(initialSize, true);
        this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).addPersistentModifier(new EntityAttributeModifier(
                "Random spawn bonus",
                this.random.nextGaussian() * 0.05D,
                EntityAttributeModifier.Operation.MULTIPLY_BASE));
        this.setLeftHanded(this.random.nextFloat() < 0.05F);

        return entityData;
    }

    // Prevents duplicate entities
    @Override
    public void remove(RemovalReason reason) {
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.emitGameEvent(GameEvent.ENTITY_KILLED);
        }
    }

    @Override
    protected ParticleEffect getParticles() {
        return ParticleTypes.SPLASH;
    }

    @Override
    protected Identifier getLootTableId() {
        return this.getType().getLootTableId();
    }

    protected void changeType(EntityType<? extends SwetEntity> type){
        if(!this.getType().equals(type) && !this.isRemoved()) {
            SwetEntity swet = (this.convertTo(type, true));
            if (swet != null) {
                swet.setSize(this.getSize(), false);
            }
            world.spawnEntity(swet);
        }
    }

    protected static boolean canAbsorb(Entity swet, Entity target) {
        return isAbsorbable(target) &&
                swet.getBoundingBox().expand(0, 0.5, 0).offset(0, 0.25, 0).intersects(target.getBoundingBox());
    }

    protected static boolean isAbsorbable(Entity entity) {
        return !(entity.isSneaking() || entity instanceof PlayerEntity playerEntity && playerEntity.getAbilities().flying);
    }

    protected static class FollowUnabsorbedTargetGoal<T extends LivingEntity> extends FollowTargetGoal<T> {
        public FollowUnabsorbedTargetGoal(MobEntity mob, Class<T> targetClass, int reciprocalChance, boolean checkVisibility, boolean checkCanNavigate, @Nullable Predicate<LivingEntity> targetPredicate) {
            super(mob, targetClass, reciprocalChance, checkVisibility, checkCanNavigate, targetPredicate);
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                target = this.target;
            }
            return super.shouldContinue() &&
                    !(canAbsorb(this.mob, this.mob.getTarget()));
        }
    }
}