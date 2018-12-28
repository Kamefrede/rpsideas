package com.kamefrede.rpsideas.rules.ranges;

import com.kamefrede.rpsideas.rules.ranges.base.AbstractRange;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 10:21 AM on 12/27/18.
 */
public class RangeBox extends AbstractRange {

    @Nonnull
    @Override
    public EnumRangeType getRangeType() {
        return EnumRangeType.BOX;
    }

    @Override
    public boolean isInRange(BlockPos pos, double x, double y, double z) {
        double x1 = getTransformedValue(X_MIN, pos.getX());
        double y1 = getTransformedValue(Y_MIN, pos.getY());
        double z1 = getTransformedValue(Z_MIN, pos.getZ());
        double x2 = getTransformedValue(X_MAX, pos.getX());
        double y2 = getTransformedValue(Y_MAX, pos.getY());
        double z2 = getTransformedValue(Z_MAX, pos.getZ());
        double x1s = getTransformedShiftedValue(X_MIN, pos.getX());
        double y1s = getTransformedShiftedValue(Y_MIN, pos.getY());
        double z1s = getTransformedShiftedValue(Z_MIN, pos.getZ());
        double x2s = getTransformedShiftedValue(X_MAX, pos.getX());
        double y2s = getTransformedShiftedValue(Y_MAX, pos.getY());
        double z2s = getTransformedShiftedValue(Z_MAX, pos.getZ());

        double xMin = min(x1, x1s, x2, x2s);
        double yMin = min(y1, y1s, y2, y2s);
        double zMin = min(z1, z1s, z2, z2s);
        double xMax = max(x1, x1s, x2, x2s);
        double yMax = max(y1, y1s, y2, y2s);
        double zMax = max(z1, z1s, z2, z2s);

        return xMin <= x && x <= xMax &&
                yMin <= y && y <= yMax &&
                zMax <= z && z <= zMin;
    }

    public static double min(double a, double b, double c, double d) {
        double minimum = a;
        if (b < minimum)
            minimum = b;
        if (c < minimum)
            minimum = c;
        if (d < minimum)
            minimum = d;

        return minimum;
    }

    public static double max(double a, double b, double c, double d) {
        double maximum = a;
        if (b > maximum)
            maximum = b;
        if (c > maximum)
            maximum = c;
        if (d > maximum)
            maximum = d;

        return maximum;
    }

    @Override
    protected void createParameters() {
        relativeParameter(X_MIN);
        relativeParameter(Y_MIN);
        relativeParameter(Z_MIN);
        relativeParameter(X_MAX);
        relativeParameter(Y_MAX);
        relativeParameter(Z_MAX);
    }
}
