package com.kamefrede.rpsideas.rules.ranges;

import com.kamefrede.rpsideas.rules.ranges.base.AbstractRange;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 11:46 AM on 12/27/18.
 */
public abstract class RangeCylinder extends AbstractRange {

    private double axisPos(double x, double y, double z) {
        Axis axis = axis();
        return axis == Axis.X ? x : (axis == Axis.Y ? y : z);
    }

    private double axisOne(double x, double y) {
        Axis axis = axis();
        if (axis == Axis.X)
            return y;
        return x;
    }

    private double axisTwo(double y, double z) {
        Axis axis = axis();
        if (axis != Axis.Z)
            return z;
        return y;
    }

    private double axisValueOne(BlockPos pos) {
        Axis axis = axis();
        if (axis == Axis.X)
            return getY(pos);
        return getX(pos);
    }

    private double axisValueTwo(BlockPos pos) {
        Axis axis = axis();
        if (axis != Axis.Z)
            return getZ(pos);
        return getY(pos);
    }

    private double axisMin(BlockPos pos) {
        Axis axis = axis();
        double axisValue = axisPos(pos.getX(), pos.getY(), pos.getZ());

        String minKey = switchAxis(axis, X_MIN, Y_MIN, Z_MIN);
        String maxKey = switchAxis(axis, X_MAX, Y_MAX, Z_MAX);

        double v1 = getTransformedValue(minKey, axisValue);
        double v2 = getTransformedValue(maxKey, axisValue);
        double v1s = getTransformedShiftedValue(minKey, axisValue);
        double v2s = getTransformedShiftedValue(maxKey, axisValue);

        return RangeBox.min(v1, v2, v1s, v2s);
    }

    private double axisMax(BlockPos pos) {
        Axis axis = axis();
        double axisValue = axis == Axis.X ? pos.getX() : (axis == Axis.Y ? pos.getY() : pos.getZ());

        String minKey = switchAxis(axis, X_MIN, Y_MIN, Z_MIN);
        String maxKey = switchAxis(axis, X_MAX, Y_MAX, Z_MAX);

        double v1 = getTransformedValue(minKey, axisValue);
        double v2 = getTransformedValue(maxKey, axisValue);
        double v1s = getTransformedShiftedValue(minKey, axisValue);
        double v2s = getTransformedShiftedValue(maxKey, axisValue);

        return RangeBox.max(v1, v2, v1s, v2s);
    }

    private <T> T switchAxis(Axis axis, T ifX, T ifY, T ifZ) {
        switch (axis) {
            case X:
                return ifX;
            case Y:
                return ifY;
            default:
                return ifZ;
        }
    }

    protected abstract Axis axis();

    @Nonnull
    @Override
    public EnumRangeType getRangeType() {
        return switchAxis(axis(),
                EnumRangeType.CYLINDER_X,
                EnumRangeType.CYLINDER_Y,
                EnumRangeType.CYLINDER_Z);
    }

    @Override
    public boolean isInRange(BlockPos pos, double x, double y, double z) {
        double pos1 = axisValueOne(pos);
        double pos2 = axisValueTwo(pos);

        double minAxis = axisMin(pos);
        double maxAxis = axisMax(pos);

        double range = getValue(RANGE);

        double rel1 = pos1 - axisOne(x, y);
        double rel2 = pos2 - axisTwo(y, z);
        double axisPos = axisPos(x, y, z);

        return minAxis <= axisPos && axisPos <= maxAxis &&
                rel1 * rel1 + rel2 * rel2 < range * range;
    }

    @Override
    protected void createParameters() {
        Axis axis = axis();
        if (axis != Axis.X)
            relativeParameter(X_POS);
        else {
            relativeParameter(X_MIN);
            relativeParameter(X_MAX);
        }

        if (axis != Axis.Y)
            relativeParameter(Y_POS);
        else {
            relativeParameter(Y_MIN);
            relativeParameter(Y_MAX);
        }

        if (axis != Axis.Z)
            relativeParameter(Z_POS);
        else {
            relativeParameter(Z_MIN);
            relativeParameter(Z_MAX);
        }

        numericParameter(RANGE);
    }

    public static class XCylinder extends RangeCylinder {
        @Override
        protected Axis axis() {
            return Axis.X;
        }
    }

    public static class YCylinder extends RangeCylinder {
        @Override
        protected Axis axis() {
            return Axis.Y;
        }
    }

    public static class ZCylinder extends RangeCylinder {
        @Override
        protected Axis axis() {
            return Axis.Z;
        }
    }
}
