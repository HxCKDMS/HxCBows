package HxCKDMS.bows.lib;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import HxCKDMS.bows.item.ItemHxCBow;
import HxCKDMS.bows.parts.Bow;
import HxCKDMS.bows.parts.Bowstring;
import HxCKDMS.bows.parts.BowstringColorful;
import HxCKDMS.bows.parts.IBowExtra;
import HxCKDMS.bows.parts.IBowPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BowHandler {
    public static IIcon blank;
    
    public static HashMap<String, Bow> bows = new HashMap<String, Bow>();
    public static HashMap<String, Bowstring> bowstrings = new HashMap<String, Bowstring>();
    public static HashMap<String, Arrow> arrows = new HashMap<String, Arrow>();
    
    public static void init() {
        BowHandler.bows.put("wood", new Bow("wood", 75));
        BowHandler.bows.put("iron", new Bow("iron", 125).setDrawTime(6));
        BowHandler.bowstrings.put("normal", new Bowstring("normal", 25));
        BowHandler.bowstrings.put("colorful", new BowstringColorful("colorful", 75));
        BowHandler.arrows.put("test", new Arrow("test"));
    }
    
    public static int getMaxDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemHxCBow)) return 0;
        return 100;
    }
    
    public static Bow getBow(ItemStack stack) {
        if (!BowHandler.isValidHxCBow(stack)) return BowHandler.bows.get("wood");
        NBTTagCompound tag = stack.getTagCompound();
        return BowHandler.bows.get(tag.getString("bow"));
    }
    
    public static Bowstring getBowstring(ItemStack stack) {
        if (BowHandler.isValidHxCBow(stack)) return BowHandler.bowstrings.get("normal");;
        NBTTagCompound tag = stack.getTagCompound();
        return BowHandler.bowstrings.get(tag.getString("bowstring"));
    }
    
    public static Arrow getArrow(ItemStack stack) {
        if (!BowHandler.isValidHxCBow(stack)) return BowHandler.arrows.get("test"); 
        return BowHandler.arrows.get("test");
    }
    
    public static IBowPart[] getBowParts(ItemStack stack) {
        return new IBowPart[] { BowHandler.getBow(stack), BowHandler.getBowstring(stack) };
    }
    
    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister iconRegister) {
        BowHandler.blank = iconRegister.registerIcon("hxcbows:blank");
        for (Bow o : BowHandler.bows.values())
            o.registerIcons(iconRegister);
        for (Bowstring o : BowHandler.bowstrings.values())
            o.registerIcons(iconRegister);
        for (Arrow o : BowHandler.arrows.values())
            o.registerIcons(iconRegister);
    }
    
    public static IBowExtra[] getExtras(ItemStack stack) {
        if (!BowHandler.isValidHxCBow(stack)) return new IBowExtra[] {};
        return new IBowExtra[]{};
    }
    
    public static boolean shouldAutoTarget(ItemStack stack) {
        if (!BowHandler.isValidHxCBow(stack)) return false;
        return stack.canEditBlocks();
    }
    
    public static boolean isValidHxCBow(ItemStack stack) {
        if (stack == null) return false;
        NBTTagCompound tag = stack.getTagCompound();
        return stack.getItem() instanceof ItemHxCBow && tag != null && tag.hasKey("bow") && tag.hasKey("bowstring");
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
            this.icons[1] = iconRegister.registerIcon("hxcbows:arrows/0/" + this.name);
            this.icons[2] = iconRegister.registerIcon("hxcbows:arrows/1/" + this.name);
            this.icons[3] = iconRegister.registerIcon("hxcbows:arrows/2/" + this.name);
        }
    }
    
    //public class
}
