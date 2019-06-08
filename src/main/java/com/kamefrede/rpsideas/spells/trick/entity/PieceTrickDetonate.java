package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.spells.enabler.styles.EnumAssemblyStyle;
import com.kamefrede.rpsideas.spells.enabler.styles.PieceStyledTrick;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.api.spell.param.ParamNumber;

import static vazkii.psi.api.spell.SpellContext.MAX_DISTANCE;

public class PieceTrickDetonate extends PieceStyledTrick {

    private SpellParam radius;

    public PieceTrickDetonate(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        double radiusVal = Math.min(32, SpellHelpers.ensurePositiveOrZero(this, radius));
        meta.addStat(EnumSpellStat.POTENCY, (int) Math.min(radiusVal, 5));
        meta.addStat(EnumSpellStat.COST, (int) Math.ceil(radiusVal * 5));

        if (radiusVal == 0)
            meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public EnumAssemblyStyle style() {
        return EnumAssemblyStyle.INFILTRATION;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double radiusVal = SpellHelpers.getBoundedNumber(this, context, radius, MAX_DISTANCE);
        boolean canExceed = hasStyle(context);

        if (radiusVal == 0.0) {
            IDetonationHandler.performDetonation(context.caster.world, context.caster, 0, entity -> entity == context.caster);
            return null;
        }
        IDetonationHandler.performDetonation(context.caster.world, context.caster, radiusVal, entity -> entity != null && IDetonationHandler.canBeDetonated(entity) &&
                (canExceed || entity.getDistanceSq(context.caster) < MAX_DISTANCE * MAX_DISTANCE));
        return null;
    }
}
