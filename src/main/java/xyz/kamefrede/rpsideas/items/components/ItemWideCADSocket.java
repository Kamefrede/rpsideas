package xyz.kamefrede.rpsideas.items.components;

import xyz.kamefrede.rpsideas.items.base.ItemComponent;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

public class ItemWideCADSocket extends ItemComponent {

    public ItemWideCADSocket() {
        super(RPSItemNames.WIDE_SOCKET);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.SOCKET;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.BANDWIDTH, 9);
        addStat(EnumCADStat.SOCKETS, 1);
    }


}
