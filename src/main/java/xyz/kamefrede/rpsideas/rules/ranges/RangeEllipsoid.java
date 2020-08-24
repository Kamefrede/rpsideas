package xyz.kamefrede.rpsideas.rules.ranges;

import xyz.kamefrede.rpsideas.rules.ranges.base.AbstractRange;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 10:21 AM on 12/27/18.
 */
public class RangeEllipsoid extends AbstractRange {

    @Nonnull
    @Override
    public EnumRangeType getRangeType() {
        return EnumRangeType.ELLIPSOID;
    }

    @Override
    public boolean isInRange(BlockPos pos, double x, double y, double z) {
        double posX = getX(pos), posY = getY(pos), posZ = getZ(pos);

        double xRange = getValue(X_RANGE);
        double yRange = getValue(Y_RANGE);
        double zRange = getValue(Z_RANGE);

        double relX = x - posX;
        double relY = y - posY;
        double relZ = z - posZ;

        return (relX * relX / (xRange * xRange)) +
                (relY * relY / (yRange * yRange)) +
                (relZ * relZ / (zRange * zRange)) <= 1;
    }

    @Override
    protected void createParameters() {
        relativeParameter(X_POS);
        relativeParameter(Y_POS);
        relativeParameter(Z_POS);
        numericParameter(X_RANGE);
        numericParameter(Y_RANGE);
        numericParameter(Z_RANGE);
    }
}
