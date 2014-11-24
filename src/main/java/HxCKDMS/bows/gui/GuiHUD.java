package HxCKDMS.bows.gui;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import HxCKDMS.bows.entity.EntityTargetFX;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiHUD extends Gui {
    /** Minecraft Client **/
    public Minecraft mc;
    
    public HashMap<EntityLiving, EntityTargetFX> targets;
    private HashMap<EntityLiving, EntityTargetFX> newTargets;
    
    public GuiHUD(Minecraft mc) {
        super();
        this.mc = mc;
        this.targets = new HashMap<EntityLiving, EntityTargetFX>();
        this.newTargets = new HashMap<EntityLiving, EntityTargetFX>();
    }
    
    @SubscribeEvent
    public void onRenderCrosshairs(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            return;
        }
        
        //GL11.glColor4f(1F, 1F, 1F, 1F);
        //GL11.glDisable(GL11.GL_LIGHTING);
        //GL11.glScalef(0.125F, 0.125F, 0.125F);
        EntityClientPlayerMP player = mc.thePlayer;
        double d = 1000000D;
        double trackRange = 64D;
        double checkDist = 0.9D;
        AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(0D, 0D, 0D, checkDist, checkDist, checkDist);
        Vec3 dir = player.getLookVec();
        Vec3 checkLoc = Vec3.createVectorHelper(player.posX + dir.xCoord * 8D, player.posY + dir.yCoord * 8D, player.posZ + dir.zCoord * 8D);
        Vec3 endLoc = checkLoc.addVector(dir.xCoord * trackRange, dir.yCoord * trackRange, dir.zCoord * trackRange);
        rayTracing:
        do {
            EntityLiving target = null;
            List<EntityLiving> ents = (List<EntityLiving>) player.worldObj.getEntitiesWithinAABB(EntityLiving.class, bb.copy().offset(checkLoc.xCoord - checkDist / 2D, checkLoc.yCoord - checkDist / 2D, checkLoc.zCoord - checkDist / 2D));
            if (ents.size() > 0) {
                for (EntityLiving e : ents) {
                    if (player.getDistanceSqToEntity(e) < 64D) break;
                    if (!targets.containsKey(e)) {
                        if (target != null) {
                            double d2 = player.getDistanceSqToEntity(e);
                            if (d2 < d) {
                                d = d2;
                                target = e;
                            }
                        } else target = e;
                    }
                }
                if (target != null) {
                    EntityTargetFX targetFX = new EntityTargetFX(player, target, 0D, 0D, 0D);
                    Minecraft.getMinecraft().effectRenderer.addEffect(targetFX);
                    this.targets.put(target, targetFX);
                    break rayTracing;
                }
            }
            
            if (Math.abs(endLoc.xCoord - checkLoc.xCoord) <= Math.abs(dir.xCoord * checkDist)) {
                checkLoc.xCoord = endLoc.xCoord;
            } else checkLoc.xCoord += dir.xCoord * checkDist;
            if (Math.abs(endLoc.yCoord - checkLoc.yCoord) <= Math.abs(dir.yCoord * checkDist)) {
                checkLoc.yCoord = endLoc.yCoord;
            } else checkLoc.yCoord += dir.yCoord * checkDist;
            if (Math.abs(endLoc.zCoord - checkLoc.zCoord) <= Math.abs(dir.zCoord * checkDist)) {
                checkLoc.zCoord = endLoc.zCoord;
            } else checkLoc.zCoord += dir.zCoord * checkDist;
            //player.worldObj.spawnParticle("smoke", checkLoc.xCoord, checkLoc.yCoord, checkLoc.zCoord, 0D, 0D, 0D);
        } while (checkLoc.xCoord != endLoc.xCoord || checkLoc.yCoord != endLoc.yCoord || checkLoc.zCoord != endLoc.zCoord);
        
        for (EntityLiving target : targets.keySet()) {
            if (!target.isDead && !targets.get(target).isDead) {
                EntityTargetFX t = targets.get(target);
                //player.worldObj.spawnParticle("smoke", t.posX, t.posY, t.posZ, 0D, 0D, 0D);
                newTargets.put(target, t);
            }
        }
        targets.clear();
        targets.putAll(newTargets);
        newTargets.clear();
        
        /*if (this.targets.size() > 0) {
            String s = "";
            for (EntityLiving target : targets) {
                s += target.getCommandSenderName() + ", ";
                
                
            }
            this.drawString(mc.fontRenderer, s.substring(0, s.length() - 2), 0, 0, 0xFFFFFFFF);
        }*/
        
        //this.drawTexturedModalRect(0, 0, 0, 0, 16, 16);
        //GL11.glScalef(1F, 1F, 1F);
    }
}