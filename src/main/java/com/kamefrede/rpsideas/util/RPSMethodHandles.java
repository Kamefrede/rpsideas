package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import com.udojava.evalex.Expression;
import com.udojava.evalex.LazyFunction;
import net.minecraft.client.gui.GuiTextField;
import org.apache.logging.log4j.Level;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.spell.SpellCompiler;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.util.Map;

import static java.lang.invoke.MethodHandles.publicLookup;

public class RPSMethodHandles {
    @Nonnull
    private static final MethodHandle spellNameFieldGetter, compilerGetter, compilerSetter, expressionFunctionGetter;

    static {
        try {
            Field f = GuiProgrammer.class.getDeclaredField("spellNameField");
            f.setAccessible(true);
            spellNameFieldGetter = publicLookup().unreflectGetter(f);

            f = GuiProgrammer.class.getDeclaredField("compiler");
            f.setAccessible(true);
            compilerGetter = publicLookup().unreflectGetter(f);
            compilerSetter = publicLookup().unreflectSetter(f);

            f = Expression.class.getDeclaredField("functions");
            f.setAccessible(true);
            expressionFunctionGetter = publicLookup().unreflectGetter(f);

        } catch (Throwable t) {
            RPSIdeas.LOGGER.log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw new RuntimeException(t);
        }
    }

    @Nonnull
    public static GuiTextField getSpellNameField(@Nonnull GuiProgrammer programmer) {
        try {
            return (GuiTextField) spellNameFieldGetter.invokeExact(programmer);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    @Nonnull
    public static SpellCompiler getSpellCompiler(@Nonnull GuiProgrammer programmer) {
        try {
            return (SpellCompiler) compilerGetter.invokeExact(programmer);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static void setSpellCompiler(@Nonnull GuiProgrammer programmer, @Nonnull SpellCompiler compiler) {
        try {
            compilerSetter.invokeExact(programmer, compiler);
        } catch (Throwable t) {
            throw propagate(t);
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
