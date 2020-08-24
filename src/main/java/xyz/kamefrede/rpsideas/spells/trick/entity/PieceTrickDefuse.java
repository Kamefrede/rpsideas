package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.effect.RPSPotions;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.potion.PotionEffect;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickDefuse extends PieceTrick {

    private SpellParam entity;
    private SpellParam time;

    public PieceTrickDefuse(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(entity = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double tim = SpellHelpers.ensurePositiveAndNonzero(this, time);
        meta.addStat(EnumSpellStat.POTENCY, (int) (tim / 20 * 50));
        meta.addStat(EnumSpellStat.COST, (int) (tim / 20 * 130));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double tim = SpellHelpers.getNumber(this, context, time, 1);
        Entity ent = SpellHelpers.ensureNonnullEntity(this, context, entity);
        if (!(ent instanceof EntityLivingBase || ent instanceof EntityTNTPrimed))
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        if (!ent.isNonBoss())
            throw new SpellRuntimeException(SpellRuntimeException.BOSS_IMMUNE);
        if (!SpellHelpers.isBlockPosInRadius(context, ent.getPosition()))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        if (ent instanceof EntityTNTPrimed) {
            EntityTNTPrimed tnt = (EntityTNTPrimed) ent;
            tnt.setFuse(tnt.getFuse() + (int) tim);
        } else {
            EntityLivingBase livingBase = (EntityLivingBase) ent;
            livingBase.addPotionEffect(new PotionEffect(RPSPotions.unseeing, (int) tim));
        }
        return null;
    }
}
