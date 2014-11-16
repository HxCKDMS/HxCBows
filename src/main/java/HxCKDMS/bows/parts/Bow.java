package HxCKDMS.bows.parts;

import HxCKDMS.bows.entity.EntityHxCArrow;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
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

    @Override
    public EntityHxCArrow applyArrowEffects(ItemStack stack, EntityHxCArrow arrow, float pullPerc) {
        if (pullPerc == 1.0F) arrow.setIsCritical(true);
        int powerModifier = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
        if (powerModifier > 0) arrow.setDamage(arrow.getDamage() + (double) powerModifier * 0.5D + 0.5D);
        int punchModifier = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
        if (punchModifier > 0) arrow.setKnockbackStrength(punchModifier);
        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack) > 0) arrow.setFire(100);
        return arrow;
    }
}
