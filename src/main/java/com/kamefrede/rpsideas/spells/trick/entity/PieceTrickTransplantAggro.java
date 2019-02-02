package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickTransplantAggro extends PieceTrick {

    private SpellParam target;
    private SpellParam bag;

    public PieceTrickTransplantAggro(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
        addParam(bag = new ParamEntity(SpellParams.GENERIC_NAME_PUNCHING_BAG, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 70);
        meta.addStat(EnumSpellStat.COST, (int) 1200);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (!context.caster.world.isRemote) {
            Entity ent = SpellHelpers.ensureNonnullEntity(this, context, target);
            EntityLivingBase punching = SpellHelpers.ensureNonnullandLivingBaseEntity(this, context, bag);
            if (!(ent instanceof EntityLiving || ent instanceof EntityTNTPrimed))
                throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
            if (ent.isNonBoss() && !punching.isNonBoss())
                throw new SpellRuntimeException(SpellRuntimeException.BOSS_IMMUNE);
            if (!SpellHelpers.isBlockPosInRadius(context, ent.getPosition()) && !SpellHelpers.isBlockPosInRadius(context, punching.getPosition()))
                throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
            if (ent instanceof EntityTNTPrimed) {
                EntityTNTPrimed tnt = (EntityTNTPrimed) ent;
                tnt.setFuse(0);
            } else {
                EntityLiving living = (EntityLiving) ent;
                living.setAttackTarget(punching);
            }
        }
        return null;
    }
}
