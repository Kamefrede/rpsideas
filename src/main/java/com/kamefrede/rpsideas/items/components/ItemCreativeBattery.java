package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

public class ItemCreativeBattery extends ItemComponent {
    public ItemCreativeBattery() {
        super(RPSItemNames.CREATIVE_BATTERY);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.BATTERY;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW, -1);
    }
}
