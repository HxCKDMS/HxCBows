package HxCKDMS.bows.parts;

import net.minecraft.item.ItemStack;
import HxCKDMS.bows.entity.EntityHxCArrow;
import HxCKDMS.bows.entity.EntityHxCSRBArrow;

public class BowstringColorful extends Bowstring {

    public BowstringColorful(String name, int durability) {
        super(name, durability);
    }
    
    @Override
    public EntityHxCArrow applyArrowEffects(ItemStack stack, EntityHxCArrow arrow, float pullPerc) {
        return new EntityHxCSRBArrow(arrow);
    }
}
