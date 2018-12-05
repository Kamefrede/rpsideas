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

import java.util.ArrayList;
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



        List<Pair<EntityPlayer, Integer>> pllist = new ArrayList<>();

        String key2 = "rpsideas:Entity" + context.caster.getEntityId() + "Broadcasted";



        AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);


        List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, axis, (Entity e) -> {
            return e != null && e instanceof EntityPlayer && e != context.caster && e != context.focalPoint && context.isInRadius(e);
        });
        if(list.size() > 0 && list != null){

            // checks if the player has broadcasted anything
            if(context.customData.containsKey(key2)){
                List<Pair<EntityPlayer, Integer>> security = (List<Pair<EntityPlayer, Integer>>) context.customData.get(key2);
                for(Pair<EntityPlayer, Integer> pairr : security){
                    EntityPlayer secPlayer = pairr.getLeft();
                    int secChan = pairr.getRight();
                    String key3 = "rpsideas:Entity" + secPlayer.getEntityId() + "NumBroadcast" + secChan;
                    if(context.customData.containsKey(key3)){
                        context.customData.remove(key3);
                    }
                }
                context.customData.remove(key2);
            }

            //actually broadcasts it!
            for(Entity ent: list){

                EntityPlayer pl = (EntityPlayer) ent;
                String key = "rpsideas:Entity" + pl.getEntityId() + "NumBroadcast" + chanInt;
                if(PsiAPI.getPlayerCAD(pl) != null){
                    if(context.customData.containsKey(key)){
                        context.customData.replace(key, signalVal);
                    } else{
                        context.customData.put(key, signalVal);
                    }
                    Pair<EntityPlayer, Integer> pair = Pair.of(pl, chanInt);
                    pllist.add(pair);
                }
            }
        }

        //no more than one broadcast per player

        context.customData.put(key2, pllist);

        //check for other instances of num broadcast
        int index = context.actions.indexOf(context.cspell.currentAction);
        for(int i = index + 1; i < context.actions.size(); i++) {
            if (context.actions.get(i).piece instanceof PieceTrickNumBroadcast) {
                context.actions.remove(i);
            }
        }
        return true;
    }
}
