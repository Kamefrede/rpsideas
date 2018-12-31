package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.entity.EntitySpellCharge;

import java.util.List;
import java.util.Objects;

public class PieceSelectorNumberCharges extends PieceSelector {

    private SpellParam position;
    private SpellParam radius;

    public PieceSelectorNumberCharges(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, true, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        SpellHelpers.ensurePositiveAndNonzero(this, radius, SpellContext.MAX_DISTANCE);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.getParamValue(context, position);
        double radiusVal = SpellHelpers.getBoundedNumber(this, context, radius, SpellContext.MAX_DISTANCE);

        if (!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        AxisAlignedBB axis = new AxisAlignedBB(positionVal.x - radiusVal, positionVal.y - radiusVal, positionVal.z - radiusVal, positionVal.x + radiusVal, positionVal.y + radiusVal, positionVal.z + radiusVal);

        List<Entity> list = context.caster.world.getEntitiesWithinAABB(Entity.class, axis,
                (Entity e) -> e != null && e != context.caster && e != context.focalPoint && context.isInRadius(e) && e instanceof EntitySpellCharge && (Objects.requireNonNull(((EntitySpellCharge) e).getThrower()).getName().equals(context.caster.getName())));

        return list.size() * 1.0;

    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
