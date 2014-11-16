package HxCKDMS.bows.lib;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import HxCKDMS.bows.item.ItemSpecialBow;
import HxCKDMS.bows.parts.Bow;
import HxCKDMS.bows.parts.Bowstring;
import HxCKDMS.bows.parts.BowstringColorful;
import HxCKDMS.bows.parts.IBowPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BowHandler {
    public static IIcon blank;
    
    public static HashMap<String, Bow> bows = new HashMap<String, Bow>();
    public static HashMap<String, Bowstring> bowstrings = new HashMap<String, Bowstring>();
    public static HashMap<String, Arrow> arrows = new HashMap<String, Arrow>();
    
    public static void init() {
        bows.put("wood", new Bow("wood", 75));
        bows.put("iron", new Bow("iron", 125).setDrawTime(6));
        bowstrings.put("normal", new Bowstring("normal", 25));
        bowstrings.put("colorful", new BowstringColorful("colorful", 75));
        arrows.put("test", new Arrow("test"));
    }
    
    public static int getMaxDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSpecialBow)) return 0;
        return 100;
    }
    
    public static Bow getBow(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSpecialBow)) return null;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("bow")) {
            return bows.get(tag.getString("bow"));
        }
        return bows.get("wood");
    }
    
    public static Bowstring getBowstring(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSpecialBow)) return null;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null && tag.hasKey("bowstring")) {
            return bowstrings.get(tag.getString("bowstring"));
        }
        return bowstrings.get("normal");
    }
    
    public static Arrow getArrow(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSpecialBow)) return null;
        return arrows.get("test");
    }
    
    public static IBowPart[] getBowParts(ItemStack stack) {
        return new IBowPart[] { getBow(stack), getBowstring(stack) };
    }
    
    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister iconRegister) {
        blank = iconRegister.registerIcon("hxcbows:blank");
        for (Bow o : bows.values()) {
            o.registerIcons(iconRegister);
        }
        for (Bowstring o : bowstrings.values()) {
            o.registerIcons(iconRegister);
        }
        for (Arrow o : arrows.values()) {
            o.registerIcons(iconRegister);
        }
    }
    
    public static class Arrow { // Temp, will split into parts
        public String name;
        public IIcon[] icons;
        
        public Arrow(String name) {
            this.name = name;
        }
        
        public void registerIcons(IIconRegister iconRegister) {
            this.icons = new IIcon[4];
            this.icons[0] = null;
            this.icons[1] = iconRegister.registerIcon("hxcbows:arrows/0/" + name);
            this.icons[2] = iconRegister.registerIcon("hxcbows:arrows/1/" + name);
            this.icons[3] = iconRegister.registerIcon("hxcbows:arrows/2/" + name);
        }
    }
    
    //public class
}
