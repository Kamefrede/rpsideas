package com.github.kamefrede.rpsideas.spells.trick.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PieceTrickOpenElytra extends PieceTrick {

    SpellParam num;

    public PieceTrickOpenElytra(Spell spell) {
        super(spell);
    }

    Method setFlag = ReflectionHelper.findMethod(Entity.class, "setFlag", "func_70052_a", int.class, boolean.class);



    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
        meta.addStat(EnumSpellStat.COST, 5);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double value = this.<Double>getParamValue(context, num);
        if((!context.caster.world.isRemote) && context.caster != null){
            if(Math.abs(value) < 1.0) {
                context.caster.getArmorInventoryList();
                Iterable<ItemStack> it = context.caster.getArmorInventoryList();
                for (ItemStack stack : it) {
                    if (stack.getItem() instanceof ItemElytra) {
                        try {
                            setFlag.invoke((Entity)context.caster, 7, true);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            System.out.println("pls contact kamefrede#4501 asap");
                        }

                        return true;
                    }
                }

            }
        }

        return false;
    }
}
