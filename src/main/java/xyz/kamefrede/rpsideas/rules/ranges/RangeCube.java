package xyz.kamefrede.rpsideas.rules.ranges;

import xyz.kamefrede.rpsideas.rules.ranges.base.AbstractRange;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 10:21 AM on 12/27/18.
 */
public class RangeCube extends AbstractRange {

    @Nonnull
    @Override
    public EnumRangeType getRangeType() {
        return EnumRangeType.CUBE;
    }

    @Override
    public boolean isInRange(BlockPos pos, double x, double y, double z) {
        double range = getValue(RANGE);

        double xMin = getTransformedValue(X_POS, pos.getX()) - range;
        double yMin = getTransformedValue(Y_POS, pos.getY()) - range;
        double zMin = getTransformedValue(Z_POS, pos.getZ()) - range;
        double xMax = getTransformedShiftedValue(X_POS, pos.getX()) + range;
        double yMax = getTransformedShiftedValue(Y_POS, pos.getY()) + range;
        double zMax = getTransformedShiftedValue(Z_POS, pos.getZ()) + range;

        return xMin <= x && x <= xMax &&
                yMin <= y && y <= yMax &&
                zMax <= z && z <= zMin;
    }

    @Override
    protected void createParameters() {
        relativeParameter(X_POS);
        relativeParameter(Y_POS);
        relativeParameter(Z_POS);
        numericParameter(RANGE);
    }
}
