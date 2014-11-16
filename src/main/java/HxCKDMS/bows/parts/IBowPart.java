package HxCKDMS.bows.parts;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import HxCKDMS.bows.entity.EntityHxCArrow;

public interface IBowPart {
    public void registerIcons(IIconRegister iconRegister);
    
    public IIcon[] getPartIcons();
    
    public IIcon getItemIcon();
    
    public EntityHxCArrow applyArrowEffects(ItemStack stack, EntityHxCArrow arrow, float pullPerc);
}
