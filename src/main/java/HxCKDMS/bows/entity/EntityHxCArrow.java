package HxCKDMS.bows.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class EntityHxCArrow extends EntityArrow {
    public ItemStack arrowStack;
    private int ticksInAir;
    public int ticksInGround;
    public int xTile = -1;
    public int yTile = -1;
    public int zTile = -1;
    public Block inTile;
    public int inData;
    public boolean inGround;
    public int knockbackStrength;
    public float speed;
    
    /** For loading **/
    public EntityHxCArrow(World worldObj) {
        super(worldObj);
    }
    
    /** For rendering random stuff **/
    public EntityHxCArrow(World worldObj, double x, double y, double z) {
        super(worldObj, x, y, z);
    }
    
    /** For players **/
    public EntityHxCArrow(World worldObj, EntityLivingBase shooter, float speed, ItemStack stack) {
        super(worldObj, shooter, speed);
        this.arrowStack = stack;
        this.speed = speed;
    }
    
    /** For mobs **/
    public EntityHxCArrow(World worldObj, EntityLivingBase shooter, EntityLivingBase target, float speed, float dirRandomness, ItemStack stack) {
        super(worldObj, shooter, target, speed, dirRandomness);
        this.arrowStack = stack;
    }
    
    @Override
    public void setThrowableHeading(double x, double y, double z, float speed, float dirRandomness) {
        super.setThrowableHeading(x, y, z, speed, dirRandomness);
        this.ticksInGround = 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double xSpeed, double ySpeed, double zSpeed) {
        super.setVelocity(xSpeed, ySpeed, zSpeed);
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) this.ticksInGround = 0;
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        // Thanks, Obama
        this.xTile = tag.getShort("xTile");
        this.yTile = tag.getShort("yTile");
        this.zTile = tag.getShort("zTile");
        this.ticksInGround = tag.getShort("life");
        this.inTile = Block.getBlockById(tag.getByte("inTile") & 255);
        this.inData = tag.getByte("inData") & 255;
        this.inGround = tag.getByte("inGround") == 1;
    }
    
    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean pickedUp = this.canBePickedUp == 1 || this.canBePickedUp == 2 && player.capabilities.isCreativeMode;
            if (this.canBePickedUp == 1 && !player.inventory.addItemStackToInventory(arrowStack)) pickedUp = false;
            
            if (pickedUp) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }
    
    public void setKnockbackStrength(int knockback) {
        this.knockbackStrength = knockback;
    }
    
    public int getKnockbackStrength() {
        return this.knockbackStrength;
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
    
    @Override
    public void onUpdate() {
        this.onEntityUpdate();
        
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float motionXZ = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) motionXZ) * 180.0D / Math.PI);
        }
        
        Block inBlock = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
        
        /*if (inBlock.getMaterial() != Material.air) {
            inBlock.setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB blockBB = inBlock.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);
            if (blockBB != null && blockBB.isVecInside(Vec3.createVectorHelper(this.posX, this.posY, this.posZ))) this.inGround = true;
        }*/
        
        if (this.arrowShake > 0) --this.arrowShake;
        
        if (this.inGround) {
            int blockData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
            
            // If hasn't moved blocks
            if (inBlock == this.inTile && blockData == this.inData) {
                ++this.ticksInGround;
                
                if (this.ticksInGround == 1200) {
                    this.setDead();
                }
            } else { // Block removed/changed
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3 posVec = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            Vec3 nextPosVec = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition moveChecker = this.worldObj.func_147447_a(posVec, nextPosVec, false, true, false);
            posVec = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
            nextPosVec = Vec3.createVectorHelper(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            
            if (moveChecker != null) nextPosVec = Vec3.createVectorHelper(moveChecker.hitVec.xCoord, moveChecker.hitVec.yCoord, moveChecker.hitVec.zCoord);
            
            Entity collEnt = null;
            List nearbyEnts = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double collDistance = 0.0D;
            int i;
            float checkDist;
            
            for (i = 0; i < nearbyEnts.size(); ++i) {
                Entity curEnt = (Entity) nearbyEnts.get(i);
                
                if (curEnt.canBeCollidedWith() && (curEnt != this.shootingEntity || this.ticksInAir >= 5)) {
                    checkDist = 0.3F;
                    AxisAlignedBB checkBB = curEnt.boundingBox.expand((double) checkDist, (double) checkDist, (double) checkDist);
                    MovingObjectPosition intersection = checkBB.calculateIntercept(posVec, nextPosVec);
                    
                    if (intersection != null) {
                        double intersectDist = posVec.distanceTo(intersection.hitVec);
                        
                        if (intersectDist < collDistance || collDistance == 0.0D) {
                            collEnt = curEnt;
                            collDistance = intersectDist;
                        }
                    }
                }
            }
            
            if (collEnt != null) {
                moveChecker = new MovingObjectPosition(collEnt);
            }
            
            if (moveChecker != null && moveChecker.entityHit != null && moveChecker.entityHit instanceof EntityPlayer) {
                EntityPlayer collPlayer = (EntityPlayer) moveChecker.entityHit;
                
                if (collPlayer.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(collPlayer)) {
                    moveChecker = null;
                }
            }
            
            float motionXYZ;
            float motionXZ;
            
            if (moveChecker != null) {
                if (moveChecker.entityHit != null) { // Hit an entity
                    motionXYZ = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int damage = MathHelper.ceiling_double_int((double) motionXYZ * this.getDamage());
                    
                    if (this.getIsCritical()) {
                        damage += this.rand.nextInt(damage / 2 + 2);
                    }
                    
                    DamageSource source = null;
                    if (this.shootingEntity == null) source = DamageSource.causeArrowDamage(this, this);
                    else source = DamageSource.causeArrowDamage(this, this.shootingEntity);
                    if (this.isBurning() && !(moveChecker.entityHit instanceof EntityEnderman)) moveChecker.entityHit.setFire(5);
                    
                    if (moveChecker.entityHit.attackEntityFrom(source, (float) damage)) { // Damaged entity
                        if (moveChecker.entityHit instanceof EntityLivingBase) {
                            EntityLivingBase collEntBase = (EntityLivingBase) moveChecker.entityHit;
                            
                            if (!this.worldObj.isRemote) collEntBase.setArrowCountInEntity(collEntBase.getArrowCountInEntity() + 1);
                            
                            // Knockback
                            if (this.getKnockbackStrength() > 0) {
                                motionXZ = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                                if (motionXZ > 0.0F) moveChecker.entityHit.addVelocity(this.motionX * (double) this.knockbackStrength * 0.6000000238418579D / (double) motionXZ, 0.1D, this.motionZ * (double) this.getKnockbackStrength() * 0.6000000238418579D / (double) motionXZ);
                            }
                            
                            // Enchantment effects
                            if (this.shootingEntity != null && this.shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a(collEntBase, this.shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, collEntBase);
                            }
                            
                            // Packet
                            if (this.shootingEntity != null && moveChecker.entityHit != this.shootingEntity && moveChecker.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                        }
                        
                        // Sound
                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                        
                        if (!(moveChecker.entityHit instanceof EntityEnderman)) this.setDead();
                    } else { // Didn't do damage
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                } else { // Hit a block
                    this.xTile = moveChecker.blockX;
                    this.yTile = moveChecker.blockY;
                    this.zTile = moveChecker.blockZ;
                    this.inTile = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (double) ((float) (moveChecker.hitVec.xCoord - this.posX));
                    this.motionY = (double) ((float) (moveChecker.hitVec.yCoord - this.posY));
                    this.motionZ = (double) ((float) (moveChecker.hitVec.zCoord - this.posZ));
                    motionXYZ = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) motionXYZ * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) motionXYZ * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) motionXYZ * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);
                    if (this.inTile.getMaterial() != Material.air) this.inTile.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
                }
            }
            
            this.onAirTick();
            
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            motionXYZ = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            
            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) motionXYZ) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }
            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }
            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }
            
            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float speedModifier = 0.99F;
            checkDist = 0.05F;
            
            if (this.isInWater()) {
                speedModifier = 0.8F;
            }
            
            if (this.isWet()) {
                this.extinguish();
            }
            
            this.motionX *= (double) speedModifier;
            this.motionY *= (double) speedModifier;
            this.motionZ *= (double) speedModifier;
            this.motionY -= (double) checkDist;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.func_145775_I();
        }
    }
    
    protected void onAirTick() {
        // Crit particles
        if (this.getIsCritical()) {
            for (int i = 0; i < 4; ++i) {
                //this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double) i / 4.0D, this.posY + this.motionY * (double) i / 4.0D, this.posZ + this.motionZ * (double) i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
            }
        }
        
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                //this.worldObj.spawnParticle("bubble", this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
            }
        }
    }
}
