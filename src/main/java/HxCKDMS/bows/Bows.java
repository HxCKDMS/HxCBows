package HxCKDMS.bows;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import HxCKDMS.bows.item.Items;
import HxCKDMS.bows.lib.BowHandler;
import HxCKDMS.bows.lib.Reference;
import HxCKDMS.bows.proxy.CommonProxy;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

//cpw.mods.fml.common.versioning.VersionRange.createFromVersionSpec
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class Bows {
    
    @SidedProxy(serverSide = "HxCKDMS.bows.proxy.ServerProxy", clientSide = "HxCKDMS.bows.proxy.ClientProxy")
    public static CommonProxy proxy;
    
    @Instance(Reference.MOD_ID)
    public static Bows instance;
    
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
        CraftingManager.getInstance().addShapelessRecipe(is.copy(), new Object[] { Block.getBlockFromName("dirt") });
        tag.setString("bow", "iron");
        CraftingManager.getInstance().addShapelessRecipe(is.copy(), new Object[] { Block.getBlockFromName("cobblestone") });
        
        // Ore dictionary
        //OreDictionary.registerOre("string", ore);
        
        
        proxy.preinit();
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
