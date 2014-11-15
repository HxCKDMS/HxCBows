package HxCKDMS.bows.parts;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class Bow implements IBowPart {
    public String name;
    public int durability;
    public IIcon[] icons;
    protected int drawTime;
    
    public Bow(String name, int durability) {
        this.name = name;
        this.durability = durability;
        this.drawTime = 18;
    }
    
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[4];
        this.icons[0] = iconRegister.registerIcon("hxcbows:bows/standby/" + name);
        this.icons[1] = iconRegister.registerIcon("hxcbows:bows/0/" + name);
        this.icons[2] = iconRegister.registerIcon("hxcbows:bows/1/" + name);
        this.icons[3] = iconRegister.registerIcon("hxcbows:bows/2/" + name);
    }

    @Override
    public IIcon[] getPartIcons() {
        return this.icons;
    }

    @Override
    public IIcon getItemIcon() {
        return this.icons[0];
    }
    
    public Bow setDrawTime(int time) {
        this.drawTime = time;
        return this;
    }
    
    public int getDrawTime() {
        return this.drawTime;
    }
}
