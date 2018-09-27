package com.github.kamefrede.rpsideas.items.components;

import com.github.kamefrede.rpsideas.items.base.ItemComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.ICADColorizer;

public class ItemEmptyColorizer extends ItemComponent implements ICADColorizer {
    @SideOnly(Side.CLIENT)
    @Override
    public int getColor(ItemStack itemStack) {
        return 0x080808;
    }
}
