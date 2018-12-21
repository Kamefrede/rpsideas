package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
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
        Double radiusVal = this.getParamEvaluation(this.radius);
        if (radiusVal == null || radiusVal <= 0.0D)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, this.x, this.y);
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;
        Entity entity = this.getParamValue(context, ent);
        Double radius = this.getParamValue(context, this.radius);
        if (!(entity instanceof EntityLivingBase))
            throw new SpellRuntimeException(SpellRuntimeExceptions.ENTITY_NOT_LIVING);
        EntityLivingBase living = (EntityLivingBase) entity;
        Vector3 pos = new Vector3(living.posX, living.posY, living.posZ);
        if (!context.isInRadius(pos))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        else {
            AxisAlignedBB axis = new AxisAlignedBB(pos.x, pos.y - radius, pos.z - radius, pos.x + radius, pos.y + radius, pos.z + radius);
            Predicate<Entity> targetPredicate = this.getTargetPredicate();
            List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, axis,
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
