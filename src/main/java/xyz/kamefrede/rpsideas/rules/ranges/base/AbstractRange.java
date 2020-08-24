package xyz.kamefrede.rpsideas.rules.ranges.base;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author WireSegal
 * Created at 11:16 AM on 12/27/18.
 */
public abstract class AbstractRange implements IRange {
    private Map<String, RelativePos> referenceInputMap = new LinkedHashMap<>();
    private Map<String, Double> referenceNumericMap = new LinkedHashMap<>();

    public AbstractRange() {
        createParameters();
    }

    protected void relativeParameter(String name) {
        referenceInputMap.put(name, new RelativePos());
    }

    protected void numericParameter(String name) {
        referenceNumericMap.put(name, 0.0);
    }

    protected abstract void createParameters();

    @Nonnull
    @Override
    public Map<String, RelativePos> getInputMap() {
        return new LinkedHashMap<>(referenceInputMap);
    }

    @Nonnull
    @Override
    public Map<String, Double> getNumericMap() {
        return new LinkedHashMap<>(referenceNumericMap);
    }
}
