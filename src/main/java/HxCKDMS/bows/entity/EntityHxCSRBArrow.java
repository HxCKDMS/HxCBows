package HxCKDMS.bows.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class EntityHxCSRBArrow extends EntityHxCArrow {
    private boolean hasAsploded = false;
    public boolean shouldAsplode = false;
    
    public EntityHxCSRBArrow(EntityHxCArrow arrow) {
        super(arrow.worldObj);
        this.canBePickedUp = arrow.canBePickedUp;
        this.setLocationAndAngles(arrow.posX, arrow.posY, arrow.posZ, arrow.rotationYaw, arrow.rotationPitch);
        this.yOffset = 0.0F;
        Vec3 dir = Vec3.createVectorHelper(arrow.motionX, arrow.motionY, arrow.motionZ).normalize();
        this.motionX = dir.xCoord * arrow.speed;
        this.motionY = dir.yCoord * arrow.speed;
        this.motionZ = dir.zCoord * arrow.speed;
        this.ticksInGround = 0;
        this.setDamage(arrow.getDamage());
        this.setKnockbackStrength(arrow.getKnockbackStrength());
        this.setIsCritical(arrow.getIsCritical());
        this.shootingEntity = arrow.shootingEntity;
    }
    
    public EntityHxCSRBArrow(World worldObj) {
        super(worldObj);
    }
    
    public EntityHxCSRBArrow(World worldObj, double x, double y, double z) {
        super(worldObj, x, y, z);
    }
    
    public EntityHxCSRBArrow(World worldObj, EntityLivingBase shooter, float speed, ItemStack stack) {
        super(worldObj, shooter, speed, stack);
    }
    
    public EntityHxCSRBArrow(World worldObj, EntityLivingBase shooter, EntityLivingBase target, float speed, float dirRandomness, ItemStack stack) {
        super(worldObj, shooter, target, speed, dirRandomness, stack);
    }
    
    @SuppressWarnings("unused")
    @Override
    public void onUpdate() {
        //if (true) return;
        
        if (this.getIsCritical()) this.shouldAsplode = true;
        
        super.onUpdate();
        if (this.shouldAsplode && !this.hasAsploded && this.ticksInAir >= 10) {
            this.hasAsploded = true;
            this.motionX *= 16D;
            this.motionY *= 16D;
            this.motionZ *= 16D;
            // Do asplosion
            
            if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) if (false) for (int n = 0; n < 360; n++) {
                double speed = 0.5D;
                float[] color = this.getColor(n);
                EntityReddustFX particle = new EntityReddustFX(this.worldObj, this.posX, this.posY, this.posZ, color[0], color[1], color[2]);
                particle.motionX = (this.rand.nextDouble() - 0.5D) * speed;
                particle.motionY = (this.rand.nextDouble() - 0.5D) * speed;
                particle.motionZ = (this.rand.nextDouble() - 0.5D) * speed;
                Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                particle.renderDistanceWeight = 5.0D;
            }
            else {
                Vec3 arrowDir = Vec3.createVectorHelper(this.motionX, this.motionY, this.motionZ);
                arrowDir = arrowDir.normalize();
                
                double speed = 0.5D;
                for (float i = 0; i < 360; i++) {
                    double theta = Math.toRadians(i);
                    Vec3 normal = Vec3.createVectorHelper(Math.cos(theta), 0D, Math.sin(theta));
                    Vec3 result = arrowDir.crossProduct(normal).normalize();
                    float[] color = this.getColor(i);
                    EntityReddustFX particle = new EntityReddustFX(this.worldObj, this.posX, this.posY, this.posZ, color[0], color[1], color[2]);
                    particle.motionX = result.xCoord * speed;
                    particle.motionY = result.yCoord * speed;
                    particle.motionZ = result.zCoord * speed;
                    Minecraft.getMinecraft().effectRenderer.addEffect(particle);
                    particle.renderDistanceWeight = 5.0D;
                }
            }
            
            this.playSound("random.explode", 4.0F, (1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F) * 0.7F);
        }
    }
    
    @Override
    protected void onAirTick() {
        super.onAirTick();
        if (this.hasAsploded && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) for (int i = 0; i < 4; ++i) {
            float[] color = this.getColor(this.ticksInAir - 10);
            EntityReddustFX particle = new EntityReddustFX(this.worldObj, this.posX + this.motionX * i / 4.0D, this.posY + this.motionY * i / 4.0D, this.posZ + this.motionZ * i / 4.0D, color[0], color[1], color[2]);
            Minecraft.getMinecraft().effectRenderer.addEffect(particle);
            particle.renderDistanceWeight = 5.0D;
        }
        if (this.ticksInAir >= 100) this.setDead();
    }
    
    private float[] getColor(float n) {
        double theta = Math.toRadians(n);
        float red = (float) Math.sin(theta) + 0.5F;
        float green = (float) Math.sin(theta + 1.05F) + 0.5F;
        float blue = (float) Math.sin(theta + 2.09F) + 0.5F;
        return new float[] { red == 0F ? 0.01F : red, green, blue };
    }
    
    @Override
    public float getGravity() {
        return this.getIsCritical() ? 0F : super.getGravity();
    }
}
