package xyz.kamefrede.rpsideas.util;

import xyz.kamefrede.rpsideas.RPSIdeas;
import com.udojava.evalex.Expression;
import com.udojava.evalex.LazyFunction;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.Map;

import static java.lang.invoke.MethodHandles.publicLookup;

public class RPSMethodHandles {
    @Nonnull
    private static final MethodHandle expressionFunctionGetter;

    static {
        try {
            Field f = Expression.class.getDeclaredField("functions");
            f.setAccessible(true);
            expressionFunctionGetter = publicLookup().unreflectGetter(f);

        } catch (Throwable t) {
            RPSIdeas.LOGGER.log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, LazyFunction> getExpressionFunctions(@Nonnull Expression expression) {
        try {
            return (Map<String, LazyFunction>) expressionFunctionGetter.invokeExact(expression);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    private static RuntimeException propagate(Throwable t) {
        RPSIdeas.LOGGER.log(Level.ERROR, "Methodhandle failed!", t);
        return new RuntimeException(t);
    }
}
