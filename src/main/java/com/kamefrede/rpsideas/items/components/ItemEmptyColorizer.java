package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.ICADColorizer;

public class ItemEmptyColorizer extends ItemComponent implements ICADColorizer {

    public ItemEmptyColorizer() {
        super(RPSItemNames.EMPTY_COLORIZER);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColor(ItemStack stack) {
        return 0x080808;
    }
}
