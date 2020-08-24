package xyz.kamefrede.rpsideas.rules.ranges.base;

import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;

import java.text.DecimalFormat;

/**
 * @author WireSegal
 * Created at 10:07 AM on 12/27/18.
 */
public class RelativePos implements INBTSerializable<NBTTagString> {
    private static final DecimalFormat FORMATTER = new DecimalFormat("0.##");

    private boolean isRelative = true;
    private double value = 0;
    private boolean canShift = true;

    @Override
    public String toString() {
        if (isRelative)
            return "~" + (value != 0 ? FORMATTER.format(value) : "");
        return FORMATTER.format(value);
    }

    public double getTrueValueWithShift(double relativeTo) {
        return getTrueValue(relativeTo) + (canShift ? 1 : 0);
    }

    public double getTrueValueWithCenter(double relativeTo) {
        return getTrueValue(relativeTo) + (canShift ? 0.5 : 0);
    }

    public double getTrueValue(double relativeTo) {
        if (isRelative)
            return relativeTo + value;
        return value;
    }

    @Override
    public NBTTagString serializeNBT() {
        return new NBTTagString(toString());
    }

    @Override
    public void deserializeNBT(NBTTagString nbt) {
        String nbtValue = nbt.getString().trim();
        if (nbtValue.startsWith("~")) {
            isRelative = true;
            nbtValue = nbtValue.substring(1);
        }

        value = 0;
        canShift = !nbtValue.contains(".");

        if (!nbtValue.isEmpty() && !nbtValue.equals(".")) {
            try {
                value = Double.parseDouble(nbtValue);
            } catch (NumberFormatException ignored) {
                // NO-OP
            }
        }
    }
}
