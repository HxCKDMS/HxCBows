package HxCKDMS.bows.gui;

import java.util.HashMap;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import HxCKDMS.bows.entity.EntityTargetFX;
import HxCKDMS.bows.item.ItemHxCBow;
import HxCKDMS.bows.lib.BowHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiHUD extends Gui {
    /** Minecraft Client **/
    public Minecraft mc;
    
    public boolean targetting;
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
        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) return;
        
        EntityClientPlayerMP player = this.mc.thePlayer;
        ItemStack useItem = player.getItemInUse();
        this.targetting = useItem != null && useItem.getItem() instanceof ItemHxCBow; 
        if (this.targetting) {
            double d = 1000000D;
            final double maxDist = 64D;
            final double minDist = 8D;
            final double checkDist = 0.9D;
            AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(0D, 0D, 0D, checkDist, checkDist, checkDist);
            Vec3 dir = player.getLookVec();
            Vec3 checkLoc = Vec3.createVectorHelper(player.posX + dir.xCoord * minDist, player.posY + dir.yCoord * minDist, player.posZ + dir.zCoord * minDist);
            Vec3 endLoc = checkLoc.addVector(dir.xCoord * maxDist, dir.yCoord * maxDist, dir.zCoord * maxDist);
            rayTracing:
            do {
                EntityLiving target = null;
                List<EntityLiving> ents = player.worldObj.getEntitiesWithinAABB(EntityLiving.class, bb.copy().offset(checkLoc.xCoord - checkDist / 2D, checkLoc.yCoord - checkDist / 2D, checkLoc.zCoord - checkDist / 2D));
                if (ents.size() > 0) {
                    for (EntityLiving e : ents) {
                        if (player.getDistanceSqToEntity(e) < minDist * minDist) break;
                        if (!this.targets.containsKey(e)) if (target != null) {
                            double d2 = player.getDistanceSqToEntity(e);
                            if (d2 < d) {
                                d = d2;
                                target = e;
                            }
                        } else target = e;
                    }
                    
                    if (target != null) {
                        EntityTargetFX targetFX = new EntityTargetFX(player, target, 0D, 0D, 0D);
                        Minecraft.getMinecraft().effectRenderer.addEffect(targetFX);
                        this.targets.put(target, targetFX);
                        break rayTracing;
                    }
                }
                
                if (Math.abs(endLoc.xCoord - checkLoc.xCoord) <= Math.abs(dir.xCoord * checkDist)) checkLoc.xCoord = endLoc.xCoord;
                else checkLoc.xCoord += dir.xCoord * checkDist;
                if (Math.abs(endLoc.yCoord - checkLoc.yCoord) <= Math.abs(dir.yCoord * checkDist)) checkLoc.yCoord = endLoc.yCoord;
                else checkLoc.yCoord += dir.yCoord * checkDist;
                if (Math.abs(endLoc.zCoord - checkLoc.zCoord) <= Math.abs(dir.zCoord * checkDist)) checkLoc.zCoord = endLoc.zCoord;
                else checkLoc.zCoord += dir.zCoord * checkDist;
                //player.worldObj.spawnParticle("smoke", checkLoc.xCoord, checkLoc.yCoord, checkLoc.zCoord, 0D, 0D, 0D);
            } while (checkLoc.xCoord != endLoc.xCoord || checkLoc.yCoord != endLoc.yCoord || checkLoc.zCoord != endLoc.zCoord);
            
            for (EntityLiving target : this.targets.keySet())
                if (!target.isDead && !this.targets.get(target).isDead) {
                    EntityTargetFX t = this.targets.get(target);
                    this.newTargets.put(target, t);
                }
            this.targets.clear();
            this.targets.putAll(this.newTargets);
            this.newTargets.clear();
        } else {
            this.targets.clear();
            this.newTargets.clear();
        }
    }
}