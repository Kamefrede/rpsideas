package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.entity.EntitySpellCircle;

public class PieceTrickBreakLoop extends PieceTrick {

    private SpellParam valueParam;

    public PieceTrickBreakLoop(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        valueParam = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, false);
        addParam(valueParam);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        // NO-OP
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double value = SpellHelpers.getNumber(this, context, valueParam, 1);

        if (Math.abs(value) < 1.0) {
            if (context.focalPoint != context.caster) {
                if (context.focalPoint instanceof EntitySpellCircle) {
                    EntitySpellCircle circle = (EntitySpellCircle) context.focalPoint;
                    NBTTagCompound circleNBT = circle.writeToNBT(new NBTTagCompound());
                    circleNBT.setInteger("timesCast", 20);
                    circleNBT.setInteger("timesAlive", 100);
                    circle.readFromNBT(circleNBT);
                } else
                    context.focalPoint.setDead();
            } else {
                PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
                data.stopLoopcast();
            }

            context.stopped = true;
        }
        return null;
    }
}
