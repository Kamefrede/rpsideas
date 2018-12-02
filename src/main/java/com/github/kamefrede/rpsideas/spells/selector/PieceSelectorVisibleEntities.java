package com.github.kamefrede.rpsideas.spells.selector;

import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
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

    SpellParam ent;
    SpellParam radius;

    public PieceSelectorVisibleEntities(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(ent = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(radius = new ParamNumber("psi.spellparam.radius", SpellParam.GREEN, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        Double radiusVal = (Double)this.getParamEvaluation(this.radius);
        if (radiusVal == null || radiusVal <= 0.0D) {
            throw new SpellCompilationException("psi.spellerror.nonpositivevalue", this.x, this.y);
        }
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }


    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.world.isRemote) return null;
        Entity entval = this.<Entity>getParamValue(context, ent);
        Double radiusVal = (Double)this.getParamValue(context, radius);
        if(!(entval instanceof EntityLivingBase)) throw new SpellRuntimeException(SpellRuntimeExceptions.ENTITY_NOT_LIVING);
        EntityLivingBase entliv = (EntityLivingBase) entval;
        Vector3 entpos = new Vector3(entliv.posX, entliv.posY, entliv.posZ);
        if (!context.isInRadius(entpos)) {
            throw new SpellRuntimeException("psi.spellerror.outsideradius");
        } else {
            AxisAlignedBB axis = new AxisAlignedBB(entpos.x, entpos.y - radiusVal, entpos.z - radiusVal, entpos.x + radiusVal, entpos.y + radiusVal, entpos.z + radiusVal);
            Predicate<Entity> pred = this.getTargetPredicate();
            List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, axis, (e) -> e != null && pred.test(e) && e != context.caster && e != context.focalPoint && context.isInRadius(e) && entliv.canEntityBeSeen(e) && e != entliv);
            return new EntityListWrapper(list);
        }
    }



    public Predicate<Entity> getTargetPredicate() {
        return Objects::nonNull;
    }
}
