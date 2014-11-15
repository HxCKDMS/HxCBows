package HxCKDMS.bows.item;

import cpw.mods.fml.common.registry.GameRegistry;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.bows.lib.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;

public class Items {

	public static ItemSpecialBow bow;
	public static ItemBowPart bowPart;
	
	public static void init() {
		LogHelper.info("Loading items", Reference.MOD_NAME);
		Items.bow = new ItemSpecialBow();
		Items.bowPart = new ItemBowPart();
		GameRegistry.registerItem(Items.bow, "HxCBow");
		GameRegistry.registerItem(Items.bowPart, "HxCBowPart");
	}
	
}
