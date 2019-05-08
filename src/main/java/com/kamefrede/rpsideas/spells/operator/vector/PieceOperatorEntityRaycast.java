package com.kamefrede.rpsideas.spells.operator.vector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

import java.util.List;

public class PieceOperatorEntityRaycast extends PieceOperator {

    private SpellParam target;
    private SpellParam vector;

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
        Vector3 ent = SpellHelpers.getVector3(this, context, target, true, false, false);
        Vector3 vec = SpellHelpers.getVector3(this, context, vector, true, false, false);
        if (getEntityLookedAt(ent.toVec3D(), vec.toVec3D(), context) == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        } else return getEntityLookedAt(ent.toVec3D(), vec.toVec3D(), context);

    }


    public static Entity getEntityLookedAt(Vec3d positionVector, Vec3d lookVector, SpellContext context) throws SpellRuntimeException {
        Entity foundEntity = null;

        final double finalDistance = 32;
        double distance = finalDistance;
        RayTraceResult pos = PieceOperatorVectorRaycast.raycast(context.caster.world, new Vector3(positionVector.x, positionVector.y, positionVector.z), new Vector3(lookVector.x, lookVector.y, lookVector.z), finalDistance);

        if (pos != null)
            distance = pos.hitVec.distanceTo(positionVector);

        Vec3d reachVector = positionVector.add(lookVector.scale(finalDistance));

        Entity lookedEntity = null;
        AxisAlignedBB aabb = new AxisAlignedBB(positionVector.x, positionVector.y, positionVector.z, reachVector.x, reachVector.y, reachVector.z).expand(1f, 1f, 1f);
        List<Entity> entitiesInBoundingBox = context.caster.world.getEntitiesWithinAABBExcludingEntity(context.caster, aabb);
        double minDistance = distance;

        for (Entity entity : entitiesInBoundingBox) {
            if (entity.canBeCollidedWith()) {
                float collisionBorderSize = entity.getCollisionBorderSize();
                AxisAlignedBB hitbox = entity.getEntityBoundingBox().grow(collisionBorderSize, collisionBorderSize, collisionBorderSize);
                RayTraceResult interceptPosition = hitbox.calculateIntercept(positionVector, reachVector);

                if (hitbox.contains(positionVector)) {
                    if (0.0D < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = 0.0D;
                    }
                } else if (interceptPosition != null) {
                    double distanceToEntity = positionVector.distanceTo(interceptPosition.hitVec);

                    if (distanceToEntity < minDistance || minDistance == 0.0D) {
                        lookedEntity = entity;
                        minDistance = distanceToEntity;
                    }
                }
            }

            if (lookedEntity != null && (minDistance < distance || pos == null))
                foundEntity = lookedEntity;
        }

        return foundEntity;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Entity.class;
    }
}












