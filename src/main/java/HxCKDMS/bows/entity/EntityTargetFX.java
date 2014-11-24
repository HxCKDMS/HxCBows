package HxCKDMS.bows.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
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
    
    public void renderParticle(Tessellator tessellator, float tickTime, float rotX, float rotXZ, float rotZ, float rotYZ, float rotXY) {
        //GL11.glDisable(GL11.GL_LIGHTING);
        
        float minU = 0F;
        float maxU = minU + 0.0625F;
        float minV = 0F;
        float maxV = minV + 0.0625F;
        float size = 0.05F * this.particleScale;
        
        float x = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) tickTime - interpPosX);
        float y = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) tickTime - interpPosY);
        float z = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) tickTime - interpPosZ);
        tessellator.setColorRGBA_F(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        tessellator.addVertexWithUV((double) (x - rotX * size - rotYZ * size), (double) (y - rotXZ * size), (double) (z - rotZ * size - rotXY * size), (double) maxU, (double) maxV);//:v
        tessellator.addVertexWithUV((double) (x - rotX * size + rotYZ * size), (double) (y + rotXZ * size), (double) (z - rotZ * size + rotXY * size), (double) maxU, (double) minV);//:^
        tessellator.addVertexWithUV((double) (x + rotX * size + rotYZ * size), (double) (y + rotXZ * size), (double) (z + rotZ * size + rotXY * size), (double) minU, (double) minV);//^:
        tessellator.addVertexWithUV((double) (x + rotX * size - rotYZ * size), (double) (y - rotXZ * size), (double) (z + rotZ * size - rotXY * size), (double) minU, (double) maxV);//v:
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
        
        if (player == null || target == null || player.isDead || target.isDead) {
            this.setDead();
            return;
        }
        
        //super.onUpdate();
        //playerLoc.xCoord + dirToTarget.xCoord * dist, playerLoc.yCoord + dirToTarget.yCoord * dist, playerLoc.zCoord + dirToTarget.zCoord * dist,
        Vec3 playerLoc = player.getPosition(1F);
        Vec3 targetLoc = Vec3.createVectorHelper(target.posX, target.posY - target.height * 0.25F, target.posZ);
        targetLoc.yCoord += target.getEyeHeight();
        Vec3 dirToTarget = playerLoc.subtract(targetLoc).normalize();
        
        float dist = (float) player.getDistanceSqToEntity(target);
        if (dist < 64F) {
            this.setDead();
            for (int _ = 0; _ < 10; _++) worldObj.spawnParticle("smoke", posX, posY, posZ, 0D, 0D, 0D);
            return;
        }
        dist = 4F;
        this.particleScale = dist / 4F;
        this.setPosition(playerLoc.xCoord + dirToTarget.xCoord * dist, playerLoc.yCoord + dirToTarget.yCoord * dist, playerLoc.zCoord + dirToTarget.zCoord * dist);
        //EntityTargetFX targetFX = new EntityTargetFX(player.worldObj, target, playerLoc.xCoord + dirToTarget.xCoord * dist, playerLoc.yCoord + dirToTarget.yCoord * dist, playerLoc.zCoord + dirToTarget.zCoord * dist, 0D, 0D, 0D);
        //Minecraft.getMinecraft().effectRenderer.addEffect(targetFX);
    }
}
