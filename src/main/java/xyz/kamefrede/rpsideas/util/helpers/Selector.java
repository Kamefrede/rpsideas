package xyz.kamefrede.rpsideas.util.helpers;

import java.util.List;
import java.util.function.Function;

/**
 * @author WireSegal
 * Created at 3:19 PM on 2/17/19.
 */
@FunctionalInterface
public interface Selector<T> extends Function<List<T>, T> {
    // NO-OP
}
