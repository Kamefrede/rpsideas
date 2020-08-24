package xyz.kamefrede.rpsideas.rules.ranges;

import xyz.kamefrede.rpsideas.rules.ranges.base.AbstractRange;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 10:21 AM on 12/27/18.
 */
public class RangeSphere extends AbstractRange {

    @Nonnull
    @Override
    public EnumRangeType getRangeType() {
        return EnumRangeType.SPHERE;
    }

    @Override
    public boolean isInRange(BlockPos pos, double x, double y, double z) {
        double posX = getX(pos), posY = getY(pos), posZ = getZ(pos);

        double range = getValue(RANGE);

        double relX = x - posX;
        double relY = y - posY;
        double relZ = z - posZ;

        return relX * relX + relY * relY + relZ * relZ <= range * range;
    }

    @Override
    protected void createParameters() {
        relativeParameter(X_POS);
        relativeParameter(Y_POS);
        relativeParameter(Z_POS);
        numericParameter(RANGE);
    }
}
