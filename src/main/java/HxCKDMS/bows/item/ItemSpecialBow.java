package HxCKDMS.bows.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import HxCKDMS.bows.entity.EntityHxCArrow;
import HxCKDMS.bows.lib.BowHandler;
import HxCKDMS.bows.parts.IBowPart;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSpecialBow extends ItemBow {
    
    public ItemSpecialBow() {
        super();
        this.setUnlocalizedName("HxCBow");
        this.setNoRepair();
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderPasses(int metadata) {
        return 3;
    }
    
    public int getFullDrawTime(ItemStack stack) {
        return BowHandler.getBow(stack).getDrawTime();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List l) {
        ItemStack is = new ItemStack(item);
        NBTTagCompound tag = new NBTTagCompound();
        is.setTagCompound(tag);
        for (String bow : BowHandler.bows.keySet()) {
            for (String bowstring : BowHandler.bowstrings.keySet()) {
                tag.setString("bow", bow);
                tag.setString("bowstring", bowstring);
                l.add(is.copy());
            }
        }
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
    public void onPlayerStoppedUsing(ItemStack stack, World worldObj, EntityPlayer player, int useTime) {
        int chargeTime = this.getMaxItemUseDuration(stack) - useTime;
        
        ArrowLooseEvent event = new ArrowLooseEvent(player, stack, chargeTime);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) return;
        chargeTime = event.charge;
        
        boolean freeShot = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, stack) > 0;
        
        if (freeShot || player.inventory.hasItem(Items.arrow)) {
            float pullPerc = (float) chargeTime / (float) this.getFullDrawTime(stack);
            // Negligible parabolic curve modifier:
            pullPerc = (pullPerc * pullPerc + pullPerc * 2.0F) / 3.0F;
            if (pullPerc < 0.1D) return;
            if (pullPerc > 1.0F) pullPerc = 1.0F;
            EntityHxCArrow arrow = new EntityHxCArrow(worldObj, player, pullPerc * 2.0F, new ItemStack(Items.arrow, 1));
            
            IBowPart[] parts = BowHandler.getBowParts(stack);
            for (IBowPart part : parts)
                arrow = part.applyArrowEffects(stack, arrow, pullPerc);
            
            stack.damageItem(1, player);
            worldObj.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 1.2F) + pullPerc * 0.5F);
            if (freeShot) arrow.canBePickedUp = 2;
            else player.inventory.consumeInventoryItem(Items.arrow);
            if (!worldObj.isRemote) worldObj.spawnEntityInWorld(arrow);
        }
    }
    
    @Override
    public int getMaxDamage(ItemStack stack) {
        return BowHandler.getMaxDamage(stack);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        BowHandler.registerIcons(iconRegister);
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass) {
        return this.getIcon(stack, renderPass, null, null, 0);
    }
    
    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        // TODO: Handle multiple render passes for different bow parts
        
        int stage = 0;
        
        if (stack == usingItem) {
            int timeUsed = this.getMaxItemUseDuration(stack) - useRemaining;
            float perc = (float) timeUsed / (float) this.getFullDrawTime(stack);
            stage = MathHelper.clamp_int((int) Math.floor(perc * 2D), 0, 2) + 1;
        }
        
        if (renderPass == 0) // String
        return BowHandler.getBowstring(stack).icons[stage];
        else if (renderPass == 1) return BowHandler.getBow(stack).icons[stage];
        else if (renderPass == 2) {
            if (stage == 0) return BowHandler.blank;
            return BowHandler.getArrow(stack).icons[stage];
        }
        
        return this.itemIcon;
    }
}
