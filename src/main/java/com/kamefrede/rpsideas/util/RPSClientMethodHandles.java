package com.kamefrede.rpsideas.util;

import com.google.common.base.Throwables;
import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.libs.LibObfuscation;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.common.spell.SpellCompiler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static java.lang.invoke.MethodHandles.publicLookup;

@SuppressWarnings("unchecked")
public class RPSClientMethodHandles {
    @Nonnull
    private static final MethodHandle spellNameFieldGetter, compilerGetter, compilerSetter,
            prevEquippedProgressMainGetter, prevEquippedProgressOffGetter,
            equippedProgressMainGetter, equippedProgressOffGetter,
            stackMainGetter, stackOffGetter,
            layersGetter,
            setModelVisibilities;

    static {
        try {
            Field f = ReflectionHelper.findField(GuiProgrammer.class, "spellNameField");
            spellNameFieldGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(GuiProgrammer.class, "compiler");
            compilerGetter = publicLookup().unreflectGetter(f);
            compilerSetter = publicLookup().unreflectSetter(f);

            f = ReflectionHelper.findField(ItemRenderer.class, LibObfuscation.ITEMRENDERER_PREVEQUIPPEDPROGRESSMAINHAND);
            prevEquippedProgressMainGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(ItemRenderer.class, LibObfuscation.ITEMRENDERER_PREVEQUIPPEDPROGRESSOFFHAND);
            prevEquippedProgressOffGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(ItemRenderer.class, LibObfuscation.ITEMRENDERER_EQUIPPEDPROGRESSMAINHAND);
            equippedProgressMainGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(ItemRenderer.class, LibObfuscation.ITEMRENDERER_EQUIPPEDPROGRESSOFFHAND);
            equippedProgressOffGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(ItemRenderer.class, LibObfuscation.ITEMRENDERER_ITEMSTACKMAINHAND);
            stackMainGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(ItemRenderer.class, LibObfuscation.ITEMRENDERER_ITEMSTACKOFFHAND);
            stackOffGetter = publicLookup().unreflectGetter(f);

            f = ReflectionHelper.findField(RenderLivingBase.class, LibObfuscation.RENDERLIVINGBASE_LAYERRENDERERS);
            layersGetter = publicLookup().unreflectGetter(f);

            Method m = ReflectionHelper.findMethod(RenderPlayer.class, LibObfuscation.RENDERPLAYER_SETMODELVISIBILITIES, LibObfuscation.RENDERPLAYER_SETMODELVISIBILITIES_OBF, AbstractClientPlayer.class);
            setModelVisibilities = publicLookup().unreflect(m);

        } catch (Throwable t) {
            RPSIdeas.LOGGER.log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
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

    public static float getPrevEquipMainHand(@Nonnull ItemRenderer renderer) {
        try {
            return (float) prevEquippedProgressMainGetter.invokeExact(renderer);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static float getPrevEquipOffHand(@Nonnull ItemRenderer renderer) {
        try {
            return (float) prevEquippedProgressOffGetter.invokeExact(renderer);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static float getEquipMainHand(@Nonnull ItemRenderer renderer) {
        try {
            return (float) equippedProgressMainGetter.invokeExact(renderer);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static float getEquipOffHand(@Nonnull ItemRenderer renderer) {
        try {
            return (float) equippedProgressOffGetter.invokeExact(renderer);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    @Nullable
    public static ItemStack getStackMainHand(@Nonnull ItemRenderer renderer) {
        try {
            return (ItemStack) stackMainGetter.invokeExact(renderer);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    @Nullable
    public static ItemStack getStackOffHand(@Nonnull ItemRenderer renderer) {
        try {
            return (ItemStack) stackOffGetter.invokeExact(renderer);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    @Nonnull
    public static <T extends EntityLivingBase> List<LayerRenderer<T>> getRenderLayers(@Nonnull RenderLivingBase<T> render) {
        try {
            return (List<LayerRenderer<T>>) layersGetter.invokeExact(render);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    public static void setModelVisibilities(@Nonnull RenderPlayer render, @Nonnull AbstractClientPlayer player) {
        try {
            setModelVisibilities.invokeExact(render, player);
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
        RPSIdeas.LOGGER.log(Level.ERROR, "Methodhandle failed!");
        t.printStackTrace();
        return Throwables.propagate(t);
    }
}
