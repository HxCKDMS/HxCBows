package HxCKDMS.bows.gui;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiHUD extends Gui {
    private static final ResourceLocation texture = new ResourceLocation("hxcbows:/textures/gui/hud.png");
    
    /** Minecraft Client **/
    public Minecraft mc;
    
    public GuiHUD(Minecraft mc) {
        super();
        this.mc = mc;
    }
    
    @SubscribeEvent
    public void onRenderCrosshairs(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            return;
        }
        // Starting position for the buff bar - 2 piels from the top left corner.
        GL11.glColor4f(1F, 1F, 1F, 1F);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.mc.renderEngine.bindTexture(GuiHUD.texture);
        //GL11.glScalef(0.125F, 0.125F, 0.125F);
        EntityClientPlayerMP player = mc.thePlayer;
        List ents = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().expand(16D, 16D, 16D));
        Iterator iEnts = ents.iterator();
        while (iEnts.hasNext()) {
            Object o = iEnts.next();
            if (o instanceof EntityLiving) {
                EntityLiving e = (EntityLiving) o;
                //float theta = Math.atan2()                
            }
        }
        
        this.drawTexturedModalRect(0, 0, 0, 0, 16, 16);
        //GL11.glScalef(1F, 1F, 1F);
    }
}