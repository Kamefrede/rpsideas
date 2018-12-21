package com.kamefrede.rpsideas.util.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.client.core.handler.ClientTickHandler;

public class ClientHelpers {
    public static int pulseColor(int mergedColorIn) {
        int add = (int) Math.sin(ClientTickHandler.ticksInGame * 0.2) * 24;
        int red = (0xFF0000 & mergedColorIn) >> 16;
        int green = (0x00FF00 & mergedColorIn) >> 8;
        int blue = 0x0000FF & mergedColorIn;
        int addedRed = MathHelper.clamp(red + add, 0, 255);
        int addedGreen = MathHelper.clamp(green + add, 0, 255);
        int addedBlue = MathHelper.clamp(blue + add, 0, 255);
        return (addedRed << 16) | (addedGreen << 8) | addedBlue;
    }

    public static int getFlowColor(ItemStack stack) {
        return ICADColorizer.DEFAULT_SPELL_COLOR;

//        ItemStack colorizer = FlowColorsHelper.getColorizer(stack);
//        if (colorizer.isEmpty()) return 0x000000;
//        else return Psi.proxy.getColorizerColor(colorizer).getRGB();
    }
}
