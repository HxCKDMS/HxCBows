package HxCKDMS.bows.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import HxCKDMS.bows.lib.BowHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBowPart extends Item {
    
    public ItemBowPart() {
        super();
        this.setUnlocalizedName("HxCBowPart");
        this.setNoRepair();
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List l) {
        ItemStack is = new ItemStack(item);
        NBTTagCompound tag = new NBTTagCompound();
        is.setTagCompound(tag);
        
        tag.setString("type", "bow");
        for (String key : BowHandler.bows.keySet()) {
            tag.setString("value", key);
            l.add(is.copy());
        }
        
        tag.setString("type", "bowstring");
        for (String key : BowHandler.bowstrings.keySet()) {
            tag.setString("value", key);
            l.add(is.copy());
        }
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            String type = tag.getString("type");
            String value = tag.getString("value");
            if (type.equals("bow") && BowHandler.bows.containsKey(value)) return BowHandler.bows.get(value).getItemIcon();
            else if (type.equals("bowstring") && BowHandler.bowstrings.containsKey(value)) return BowHandler.bowstrings.get(value).getItemIcon();
        }
        return BowHandler.blank;
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack stack) {
        return this.getIcon(stack, 0);
    }
}
