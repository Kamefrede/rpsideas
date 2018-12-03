package com.github.kamefrede.rpsideas.spells.trick.entity;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.List;

public class PieceTrickNumBroadcast extends PieceTrick {

    SpellParam radius;
    SpellParam channel;
    SpellParam position;
    SpellParam signal;

    public PieceTrickNumBroadcast(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(channel = new ParamNumber(SpellParams.GENERIC_NAME_CHANNEL, SpellParam.RED, true, true));
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(signal = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.YELLOW,false , false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double radiusVal = this.<Double>getParamEvaluation(radius);
        Double channelVal = this.<Double>getParamEvaluation(channel);

        if(channelVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
        if(radiusVal == null || radiusVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
        meta.addStat(EnumSpellStat.COST, radiusVal.intValue() * 75);
        meta.addStat(EnumSpellStat.POTENCY, 5);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Double radiusVal = this.<Double>getParamValue(context, radius);
        Double channelVal = this.<Double>getParamValue(context, channel);
        Double signalVal = this.<Double>getParamValue(context, signal);

        if(signalVal == null)
            throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_NUMBER);

        if(channelVal == null) channelVal = 0D;
        int chanInt = channelVal.intValue();

        if(!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        Pair<Integer, Double> pair = Pair.of(chanInt, signalVal);



        AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);


        List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, axis, (Entity e) -> {
            return e != null && e instanceof EntityPlayer && e != context.caster && e != context.focalPoint && context.isInRadius(e);
        });
        if(list.size() > 0 && list != null){
            for(Entity ent: list){

                EntityPlayer pl = (EntityPlayer) ent;
                String key = "rpsideas:Entity" + pl.getEntityId() + "NumBroadcast";
                if(PsiAPI.getPlayerCAD(pl) != null){
                    if(context.customData.containsKey(key)){
                        context.customData.replace(key, pair);
                    } else{
                        context.customData.put(key, pair);
                    }
                }
            }
        }
        int index = context.actions.indexOf(context.cspell.currentAction);
        for(int i = index + 1; i < context.actions.size(); i++) {
            if (context.actions.get(i).piece instanceof PieceTrickNumBroadcast) {
                context.actions.remove(i);
            }
        }
        return true;
    }
}
