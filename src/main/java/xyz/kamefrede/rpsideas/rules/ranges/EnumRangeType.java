package xyz.kamefrede.rpsideas.rules.ranges;

import com.google.common.collect.Maps;
import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.rules.ranges.base.IRange;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author WireSegal
 * Created at 10:08 AM on 12/27/18.
 */
public enum EnumRangeType {
    SPHERE("sphere", RangeSphere::new),
    CUBE("cube", RangeCube::new),
    ELLIPSOID("ranges", RangeEllipsoid::new),
    CYLINDER_X("tube.x", RangeCylinder.XCylinder::new),
    CYLINDER_Y("tube.y", RangeCylinder.YCylinder::new),
    CYLINDER_Z("tube.y", RangeCylinder.ZCylinder::new),
    BOX("rect", RangeBox::new);

    private static final Map<String, EnumRangeType> NBT_REVERSE_LOOKUP = Maps.newHashMap();

    static {
        for (EnumRangeType type : values())
            NBT_REVERSE_LOOKUP.put(type.getNBTKey(), type);
    }

    private final String nbtKey;
    private final Supplier<IRange> createNew;
    EnumRangeType(String nbtKey, Supplier<IRange> createNew) {
        this.nbtKey = nbtKey;
        this.createNew = createNew;
    }

    public static EnumRangeType byKey(String type) {
        return NBT_REVERSE_LOOKUP.get(type);
    }

    public String getLangKey() {
        return RPSIdeas.MODID + ".volumetric." + getNBTKey();
    }

    public String getNBTKey() {
        return nbtKey;
    }

    public IRange createNewRange() {
        return createNew.get();
    }
}
