package HxCKDMS.bows.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import HxCKDMS.bows.lib.Reference;

public class EntityTargetFX extends EntityFX {
    private static final ResourceLocation texture = new ResourceLocation("hxcbows:/textures/gui/hud.png");
    
    public EntityLiving target;
    public EntityPlayer player;
    
    public EntityTargetFX(EntityPlayer player, EntityLiving target, double motX, double motY, double motZ) {
        super(player.worldObj, target.posX, target.posY, target.posZ, motX, motY, motZ);
        this.player = player;
        this.target = target;
        //this.setParticleTextureIndex(0);
        this.particleMaxAge = 1;
        this.motionX = motX;
        this.motionY = motY;
        this.motionZ = motZ;
        this.particleGravity = 0F;
        this.particleRed = 1F;
        this.particleGreen = 1F;
        this.particleBlue = 1F;
        this.particleAlpha = 1F;
        this.particleScale = 1F;
    }
    
    @Override
    public void renderParticle(Tessellator tessellator, float tickTime, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        //GL11.glDisable(GL11.GL_LIGHTING);
        
        float minU = 0F;
        float maxU = minU + 0.0625F;
        float minV = 0F;
        float maxV = minV + 0.0625F;
        float size = 0.05F * this.particleScale;
        
        float x = (float) (this.prevPosX + (this.posX - this.prevPosX) * tickTime - EntityFX.interpPosX);
        float y = (float) (this.prevPosY + (this.posY - this.prevPosY) * tickTime - EntityFX.interpPosY);
        float z = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * tickTime - EntityFX.interpPosZ);
        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tessellator.addVertexWithUV(x - rotX * size - rotYZ * size, y - rotXZ * size, z - rotZ * size - rotXY * size, maxU, maxV);//:v
        tessellator.addVertexWithUV(x - rotX * size + rotYZ * size, y + rotXZ * size, z - rotZ * size + rotXY * size, maxU, minV);//:^
        tessellator.addVertexWithUV(x + rotX * size + rotYZ * size, y + rotXZ * size, z + rotZ * size + rotXY * size, minU, minV);//^:
        tessellator.addVertexWithUV(x + rotX * size - rotYZ * size, y - rotXZ * size, z + rotZ * size - rotXY * size, minU, maxV);//v:
        Minecraft.getMinecraft().renderEngine.bindTexture(EntityTargetFX.texture);
        tessellator.draw();
        Minecraft.getMinecraft().renderEngine.bindTexture(Reference.particleTextures);
        tessellator.startDrawingQuads();
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        if (this.player == null || this.target == null || this.player.isDead || this.target.isDead) {
            this.setDead();
            return;
        }
        
        //super.onUpdate();
        //playerLoc.xCoord + dirToTarget.xCoord * dist, playerLoc.yCoord + dirToTarget.yCoord * dist, playerLoc.zCoord + dirToTarget.zCoord * dist,
        Vec3 playerLoc = this.player.getPosition(1F);
        Vec3 targetLoc = Vec3.createVectorHelper(this.target.posX, this.target.posY - this.target.height * 0.25F, this.target.posZ);
        targetLoc.yCoord += this.target.getEyeHeight();
        Vec3 dirToTarget = playerLoc.subtract(targetLoc).normalize();
        
        float dist = (float) this.player.getDistanceSqToEntity(this.target);
        if (dist < 64F) {
            this.setDead();
            for (int _ = 0; _ < 10; _++)
                this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, 0D, 0D, 0D);
            return;
        }
        dist = 4F;
        this.particleScale = dist / 4F;
        this.setPosition(playerLoc.xCoord + dirToTarget.xCoord * dist, playerLoc.yCoord + dirToTarget.yCoord * dist, playerLoc.zCoord + dirToTarget.zCoord * dist);
        //EntityTargetFX targetFX = new EntityTargetFX(player.worldObj, target, playerLoc.xCoord + dirToTarget.xCoord * dist, playerLoc.yCoord + dirToTarget.yCoord * dist, playerLoc.zCoord + dirToTarget.zCoord * dist, 0D, 0D, 0D);
        //Minecraft.getMinecraft().effectRenderer.addEffect(targetFX);
    }
}
