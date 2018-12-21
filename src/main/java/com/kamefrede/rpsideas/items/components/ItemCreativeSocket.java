package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

public class ItemCreativeSocket extends ItemComponent {
    public ItemCreativeSocket() {
        super(RPSItemNames.CREATIVE_SOCKET);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.SOCKET;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.BANDWIDTH, -1);
        addStat(EnumCADStat.SOCKETS, 12);
    }
}
