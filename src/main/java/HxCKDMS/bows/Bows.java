package HxCKDMS.bows;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import HxCKDMS.bows.entity.EntityHxCArrow;
import HxCKDMS.bows.entity.EntityHxCSRBArrow;
import HxCKDMS.bows.gui.GuiHUD;
import HxCKDMS.bows.item.Items;
import HxCKDMS.bows.lib.BowHandler;
import HxCKDMS.bows.lib.Reference;
import HxCKDMS.bows.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;

//cpw.mods.fml.common.versioning.VersionRange.createFromVersionSpec
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class Bows {
    
    @SidedProxy(serverSide = "HxCKDMS.bows.proxy.ServerProxy", clientSide = "HxCKDMS.bows.proxy.ClientProxy")
    public static CommonProxy proxy;
    
    @Instance(Reference.MOD_ID)
    public static Bows instance;
    
    public static GuiHUD hud;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Items.init();
        BowHandler.init();
        
        // Temp Recipes
        ItemStack is = new ItemStack(Items.bow);
        NBTTagCompound tag = new NBTTagCompound();
        is.setTagCompound(tag);
        tag.setString("bow", "wood");
        tag.setString("bowstring", "normal");
        tag.setString("bow", "iron");
        
        // Ore dictionary
        //OreDictionary.registerOre("string", ore);
        
        EntityRegistry.registerModEntity(EntityHxCArrow.class, "HxCArrow", 666, this, 64, 20, true);
        EntityRegistry.registerModEntity(EntityHxCSRBArrow.class, "HxCSRBArrow", 667, this, 64, 20, true);
        
        proxy.preinit();
        MinecraftForge.EVENT_BUS.register(Bows.hud = new GuiHUD(Minecraft.getMinecraft()));
        FMLCommonHandler.instance().bus().register(Bows.hud);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postinit();
    }
}
