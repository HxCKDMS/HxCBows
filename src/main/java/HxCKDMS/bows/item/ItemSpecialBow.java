package HxCKDMS.bows.item;

import HxCKDMS.bows.lib.BowHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSpecialBow extends ItemBow {
    
    public IIcon[] icons;
    
    public ItemSpecialBow() {
        super();
        this.setUnlocalizedName("HxCBow");
        this.setNoRepair();
    }
    
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldObj, EntityPlayer player) {
        ArrowNockEvent event = new ArrowNockEvent(player, stack);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return event.result;
        if (player.capabilities.isCreativeMode || player.inventory.hasItem(Items.arrow)) player.setItemInUse(stack, this.getMaxItemUseDuration(stack));
        ;
        return stack;
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return BowHelper.getMaxDamage(stack);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.itemIcon = iconRegister.registerIcon("HxCBows:bow_standby");
        //this.itemIcon = IIco
        
        this.icons = new IIcon[3];
        for (int i = 0; i < 3; i++) {
            this.icons[i] = iconRegister.registerIcon("HxCBows:bow_pulling_" + i);
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getItemIconForUseDuration(int duration) {
        return this.icons[duration];
    }
}
