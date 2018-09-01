package com.github.kamefrede.psiaddon.spells.trick;

import com.github.kamefrede.psiaddon.capability.stasis.damage.IStasisDamage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

import static com.github.kamefrede.psiaddon.capability.stasis.damage.LibDamage.requireStasisDamage;
import static com.github.kamefrede.psiaddon.capability.stasis.damage.StasisDamageStorage.CAPABILITY_STASIS_DAMAGE;
import static com.github.kamefrede.psiaddon.capability.stasis.time.LibTime.addTimeFromEvent;
import static com.github.kamefrede.psiaddon.util.LibPotions.stasisPotionEffect;
import static com.github.kamefrede.psiaddon.capability.stasis.time.LibTime.requireStasisTime;


public class PieceTrickEidosSuspend extends PieceTrick {

    SpellParam target;
    SpellParam time;

    public PieceTrickEidosSuspend(Spell spell){
        super(spell);
    }

    @Override
    public void initParams(){
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException{
        super.addToMetadata(meta);
        Double timeVal = this.<Double>getParamEvaluation(time);

        if(timeVal == null || timeVal <= 0 || timeVal.doubleValue() != timeVal.intValue()){
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);
        }

        meta.addStat(EnumSpellStat.POTENCY, timeVal.intValue() * 40);
        meta.addStat(EnumSpellStat.COST, timeVal.intValue() * 65);

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException{

        Entity targetVal = this.<Entity>getParamValue(context, target);
        Double timeVal = this.<Double>getParamValue(context, time);
        if(targetVal instanceof  EntityLiving && !(targetVal instanceof EntityPlayer)){
            EntityLiving ent = ((EntityLiving)targetVal);
            requireStasisTime(ent);
            requireStasisDamage(ent);
            if(!ent.isNonBoss()){
                // code for bosses
                addTimeFromEvent(ent, Math.floor(timeVal * 20 * 0.3));
                ent.setNoAI(true);
                ent.setNoGravity(true);
                ent.setVelocity(0D,0D,0D);
                ent.velocityChanged = true;
            } else {
                if(ent.getHealth() > 40){
                    addTimeFromEvent(ent, Math.floor(timeVal * 20 * 0.7));
                    ent.setNoAI(true);
                    ent.setNoGravity(true);
                    ent.setVelocity(0D,0D,0D);
                    ent.velocityChanged = true;
                } else {
                    addTimeFromEvent(ent, (timeVal * 20));
                    ent.setNoAI(true);
                    ent.setNoGravity(true);
                    ent.setVelocity(0D,0D,0D);
                    ent.velocityChanged = true;
                }
            }


        } else {
            // eventually code for boats
        }

        return null;
    }



}
