package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class PieceOperatorEntityRaycast extends PieceOperator {

    SpellParam target;
    SpellParam vector;

    public PieceOperatorEntityRaycast(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(vector = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Entity ent = this.<Entity>getParamValue(context, target);
        Vector3 vec = this.<Vector3>getParamValue(context, vector);
        if(context.caster.world.isRemote) return null;
        if(ent == null) throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        if(vec == null || vec.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if(getFirstRaycastedEntity(ent, vec) == null){
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        } else return getFirstRaycastedEntity(ent, vec);

    }


    public Entity getFirstRaycastedEntity(Entity e, Vector3 vector) throws SpellRuntimeException{
        final double maxDist = 32;
        double dist = maxDist;
        Vec3d positionVector = e.getPositionVector();
        Vec3d raycastVec = new Vec3d(vector.x * 32 , vector.y * 32, vector.z * 32);
        Entity found = null;
        if(e instanceof EntityPlayer)
            positionVector = positionVector.add(0, e.getEyeHeight(), 0);


        AxisAlignedBB raycastAABB = new AxisAlignedBB(positionVector.x, positionVector.y, positionVector.z, positionVector.x + raycastVec.x, positionVector.y + raycastVec.y, positionVector.z + raycastVec.z);
        List<Entity> allEntities = e.getEntityWorld().getEntitiesWithinAABBExcludingEntity(e, raycastAABB);
        double d0 = -1.0D;

        for (int j2 = 0; j2 < allEntities.size(); ++j2)
        {
            Entity ent1 = allEntities.get(j2);


                double d1 = ent1.getDistanceSq(e.posX, e.posY, e.posZ);

                if ((dist < 0.0D || d1 < dist * dist) && (d0 == -1.0D || d1 < d0))
                {
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












