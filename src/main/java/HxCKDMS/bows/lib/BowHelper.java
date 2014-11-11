package HxCKDMS.bows.lib;

import net.minecraft.item.ItemStack;
import HxCKDMS.bows.item.ItemSpecialBow;

public class BowHelper {
    
    public static int getMaxDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSpecialBow)) return 0;
        
        return 100;
    }
    
}
