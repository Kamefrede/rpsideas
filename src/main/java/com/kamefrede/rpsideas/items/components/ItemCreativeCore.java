package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

public class ItemCreativeCore extends ItemComponent {

    public ItemCreativeCore() {
        super(RPSItemNames.CREATIVE_CORE);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.CORE;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.COMPLEXITY, -1);
        addStat(EnumCADStat.PROJECTION, -1);
    }
}
