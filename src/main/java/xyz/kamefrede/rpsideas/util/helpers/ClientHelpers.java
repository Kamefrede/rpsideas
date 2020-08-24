package xyz.kamefrede.rpsideas.util.helpers;

import com.teamwizardry.librarianlib.core.client.ClientTickHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import vazkii.psi.common.Psi;

public class ClientHelpers {
    public static int pulseColor(int source) {
        return pulseColor(source, 1f, 0.2f, 24);
    }

    public static int pulseColor(int source, float speed, int magnitude) {
        return pulseColor(source, 1f, speed, magnitude);
    }

    public static int pulseColor(int source, float multiplier, float speed, int magnitude) {
        int add = (int) (MathHelper.sin(ClientTickHandler.getTicksInGame() * speed) * magnitude);
        int red = (0xFF0000 & source) >> 16;
        int green = (0x00FF00 & source) >> 8;
        int blue = 0x0000FF & source;
        int addedRed = MathHelper.clamp((int) (multiplier * (red + add)), 0, 255);
        int addedGreen = MathHelper.clamp((int) (multiplier * (green + add)), 0, 255);
        int addedBlue = MathHelper.clamp((int) (multiplier * (blue + add)), 0, 255);
        return (addedRed << 16) | (addedGreen << 8) | addedBlue;
    }

    public static int slideColor(int color, int secondColor, float speed) {
        float shift = (MathHelper.sin(ClientTickHandler.getTicksInGame() * speed) + 1) / 2;
        if (shift == 0)
            return color;
        else if (shift == 1)
            return secondColor;

        int redA = (0xFF0000 & color) >> 16;
        int greenA = (0x00FF00 & color) >> 8;
        int blueA = 0x0000FF & color;
        int redB = (0xFF0000 & secondColor) >> 16;
        int greenB = (0x00FF00 & secondColor) >> 8;
        int blueB = 0x0000FF & secondColor;

        int newRed = (int) (redA * (1 - shift) + redB * shift);
        int newGreen = (int) (greenA * (1 - shift) + greenB * shift);
        int newBlue = (int) (blueA * (1 - shift) + blueB * shift);
        return (newRed << 16) | (newGreen << 8) | newBlue;
    }

    public static int getFlowColor(ItemStack stack) {
        ItemStack colorizer = FlowColorsHelper.getColorizer(stack);
        if (colorizer.isEmpty()) return 0x000000;
        else return Psi.proxy.getColorForColorizer(colorizer);
    }
}
