package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.List;

public class PieceOperatorEntityRaycast extends PieceOperator {// TODO: 12/15/18 look at

    SpellParam target;
    SpellParam vector;

    public PieceOperatorEntityRaycast(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(vector = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 ent = this.<Vector3>getParamValue(context, target);
        Vector3 vec = this.<Vector3>getParamValue(context, vector);
        if (context.caster.world.isRemote) return null;
        if (ent == null || ent.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        if (vec == null || vec.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (getFirstRaycastedEntity(context, vec, ent) == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        } else return getFirstRaycastedEntity(context, vec, ent);

    }


    public Entity getFirstRaycastedEntity(SpellContext context, Vector3 vector, Vector3 target) throws SpellRuntimeException {
        final double maxDist = 32;
        double dist = maxDist;
        World world = context.caster.world;
        Vec3d positionVector = new Vec3d(target.x, target.y, target.z);
        Vec3d raycastVec = new Vec3d(vector.x * 32, vector.y * 32, vector.z * 32);
        Entity found = null;


        AxisAlignedBB raycastAABB = new AxisAlignedBB(positionVector.x, positionVector.y, positionVector.z, positionVector.x + raycastVec.x, positionVector.y + raycastVec.y, positionVector.z + raycastVec.z);
        List<Entity> allEntities = world.getEntitiesWithinAABBExcludingEntity(context.caster, raycastAABB);
        double d0 = -1.0D;

        for (int j2 = 0; j2 < allEntities.size(); ++j2) {
            Entity ent1 = allEntities.get(j2);


            double d1 = ent1.getDistanceSq(positionVector.x, positionVector.y, positionVector.z);

            if ((dist < 0.0D || d1 < dist * dist) && (d0 == -1.0D || d1 < d0)) {
                d0 = d1;
                found = ent1;
            }
        }

        return found;


    }


    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}












