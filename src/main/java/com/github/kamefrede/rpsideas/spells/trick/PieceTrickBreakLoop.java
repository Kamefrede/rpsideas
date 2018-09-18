package com.github.kamefrede.rpsideas.spells.trick;

import net.minecraft.nbt.NBTTagCompound;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.entity.EntitySpellCircle;

public class PieceTrickBreakLoop extends PieceTrick {
    public PieceTrickBreakLoop(Spell spell) {
        super(spell);
    }

   SpellParam valueParam;

    @Override
    public void initParams() {
        valueParam = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, false);
        addParam(valueParam);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double value = this.<Double>getParamValue(context, valueParam);

        if(Math.abs(value) < 1.0) {
            if(context.focalPoint != context.caster) {
                if(context.focalPoint instanceof EntitySpellCircle) {
                    //Cause the spell circle to finish early
                    EntitySpellCircle circle = (EntitySpellCircle) context.focalPoint;
                    NBTTagCompound circleNBT = circle.writeToNBT(new NBTTagCompound());
                    circleNBT.setInteger("timesCast", 20);
                    circleNBT.setInteger("timesAlive", 100);
                    circle.readFromNBT(circleNBT);
                } else {
                    //I dunno what's casting this spell, just get rid of it
                    context.focalPoint.setDead();
                }
            } else {
                //Casting from loopcast or something
                PlayerDataHandler.get(context.caster).stopLoopcast();
            }

            context.stopped = true;
        }
        return null;
    }
}
