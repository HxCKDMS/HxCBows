package HxCKDMS.bows.item;

import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.bows.lib.Reference;
import cpw.mods.fml.common.registry.GameRegistry;

public class Items {
    
    public static ItemHxCBow bow;
    public static ItemBowPart bowPart;
    
    public static void init() {
        LogHelper.info("Loading items", Reference.MOD_NAME);
        Items.bow = new ItemHxCBow();
        Items.bowPart = new ItemBowPart();
        GameRegistry.registerItem(Items.bow, "HxCBow");
        GameRegistry.registerItem(Items.bowPart, "HxCBowPart");
    }
    
}
