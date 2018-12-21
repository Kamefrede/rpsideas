package com.kamefrede.rpsideas.util.helpers;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IItemColorProvider;

public interface IFlowColorAcceptor extends IItemColorProvider {
    @Override
    @SideOnly(Side.CLIENT)
    default IItemColor getItemColor() {
        return (stack, tintIndex) -> {
            if (tintIndex == 1) {
                return ClientHelpers.getFlowColor(stack);
            } else return -1;
        };
    }
}
