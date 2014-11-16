package HxCKDMS.bows.entity;

public class EntityHxCSRBArrow extends EntityHxCArrow {
    
    public EntityHxCSRBArrow(EntityHxCArrow arrow) {
        super(arrow.worldObj);
        this.canBePickedUp = arrow.canBePickedUp;
        this.setLocationAndAngles(arrow.posX, arrow.posY, arrow.posZ, arrow.rotationYaw, arrow.rotationPitch);
        this.yOffset = 0.0F;
        this.motionX = arrow.motionX;
        this.motionY = arrow.motionY;
        this.motionZ = arrow.motionZ;
        this.ticksInGround = 0;
        this.setDamage(arrow.getDamage());
        this.setKnockbackStrength(arrow.getKnockbackStrength());
        this.setIsCritical(arrow.getIsCritical());
    }
    
    protected void onAirTick() {
        super.onAirTick();
        for (int i = 0; i < 4; ++i) {
            this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }
}
