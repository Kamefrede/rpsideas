package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.client.gui.GuiTextField;
import org.apache.logging.log4j.Level;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.spell.SpellCompiler;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

import static java.lang.invoke.MethodHandles.publicLookup;

@SuppressWarnings("unchecked")
public class RPSProgrammerMethodHandles {
    @Nonnull
    private static final MethodHandle spellNameFieldGetter, compilerGetter, compilerSetter;

    static {
        try {
            Field f = GuiProgrammer.class.getDeclaredField("spellNameField");
            f.setAccessible(true);
            spellNameFieldGetter = publicLookup().unreflectGetter(f);

            f = GuiProgrammer.class.getDeclaredField("compiler");
            f.setAccessible(true);
            compilerGetter = publicLookup().unreflectGetter(f);
            compilerSetter = publicLookup().unreflectSetter(f);

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

    private static RuntimeException propagate(Throwable t) {
        RPSIdeas.LOGGER.log(Level.ERROR, "Methodhandle failed!", t);
        return new RuntimeException(t);
    }
}
