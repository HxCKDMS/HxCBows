package HxCKDMS.bows;

import HxCKDMS.bows.item.Items;
import HxCKDMS.bows.lib.Reference;
import HxCKDMS.bows.proxy.CommonProxy;
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
		proxy.preinit();
		Items.init();
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
