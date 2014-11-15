package HxCKDMS.bows.parts;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public interface IBowPart {
    public void registerIcons(IIconRegister iconRegister);
    
    public IIcon[] getPartIcons();
    
    public IIcon getItemIcon();
}
