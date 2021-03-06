package HxCKDMS.bows.parts;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import HxCKDMS.bows.entity.EntityHxCArrow;

public class Bowstring implements IBowPart {
    public String name;
    public int durability;
    public IIcon[] icons;
    
    public Bowstring(String name, int durability) {
        this.name = name;
        this.durability = durability;
    }
    
    @Override
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[4];
        this.icons[0] = iconRegister.registerIcon("hxcbows:bowstrings/standby/" + this.name);
        this.icons[1] = iconRegister.registerIcon("hxcbows:bowstrings/0/" + this.name);
        this.icons[2] = iconRegister.registerIcon("hxcbows:bowstrings/1/" + this.name);
        this.icons[3] = iconRegister.registerIcon("hxcbows:bowstrings/2/" + this.name);
    }
    
    @Override
    public IIcon[] getPartIcons() {
        return this.icons;
    }
    
    @Override
    public IIcon getItemIcon() {
        //return ((Item) Item.itemRegistry.getObject("string")).getIconFromDamage(0);
        return this.icons[0];
    }
    
    @Override
    public EntityHxCArrow applyArrowEffects(ItemStack stack, EntityHxCArrow arrow, float pullPerc) {
        return arrow;
    }
}
