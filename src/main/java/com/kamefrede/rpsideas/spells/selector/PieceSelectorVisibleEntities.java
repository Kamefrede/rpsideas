package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PieceSelectorVisibleEntities extends PieceSelector {

    private SpellParam ent;
    private SpellParam radius;

    public PieceSelectorVisibleEntities(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(ent = new ParamEntity(SpellParams.GENERIC_NAME_VIEWER, SpellParam.RED, false, false));
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.GREEN, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        SpellHelpers.ensurePositiveAndNonzero(this, radius);
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityLivingBase living = SpellHelpers.ensureNonnullandLivingBaseEntity(this, context, ent);

        double radiusVal = SpellHelpers.getBoundedNumber(this, context, radius, SpellContext.MAX_DISTANCE);

        Vector3 pos = Vector3.fromEntity(living);
        if (!context.isInRadius(pos))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        else {
            AxisAlignedBB axis = new AxisAlignedBB(pos.x, pos.y - radiusVal, pos.z - radiusVal, pos.x + radiusVal, pos.y + radiusVal, pos.z + radiusVal);
            Predicate<Entity> targetPredicate = this.getTargetPredicate();
            List<Entity> list = context.caster.world.getEntitiesWithinAABB(Entity.class, axis,
                    (e) -> e != null && targetPredicate.test(e) &&
                            e != context.caster &&
                            e != context.focalPoint &&
                            context.isInRadius(e) &&
                            living.canEntityBeSeen(e) &&
                            e != living);
            return new EntityListWrapper(list);
        }
    }


    public Predicate<Entity> getTargetPredicate() {
        return Objects::nonNull;
    }
}
