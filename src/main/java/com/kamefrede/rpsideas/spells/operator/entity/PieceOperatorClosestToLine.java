package com.kamefrede.rpsideas.spells.operator.entity;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceOperatorClosestToLine extends PieceOperator {

    private SpellParam rayStartParam;
    private SpellParam rayEndParam;
    private SpellParam entList;
    private SpellParam length;

    public PieceOperatorClosestToLine(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        super.initParams();
        addParam(rayStartParam = new ParamVector(SpellParams.GENERIC_NAME_RAY_START, SpellParam.RED, false, false));
        addParam(rayEndParam = new ParamVector(SpellParams.GENERIC_NAME_RAY_END, SpellParam.BLUE, false, false));
        addParam(entList = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST, SpellParam.GREEN, false, false));
        addParam(length = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.CYAN, true, true));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 rayStart = this.getParamValue(context, rayStartParam);
        Vector3 rayEnd = this.getParamValue(context, rayEndParam);
        if (rayStart == null || rayEnd == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        Vec3d origin = rayStart.toVec3D();
        Vec3d rayVector = rayEnd.toVec3D().subtract(origin);
        Vec3d ray = rayVector.normalize();
        double len = rayVector.length();

        EntityListWrapper list = this.getParamValue(context, entList);
        if (list == null)
            throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_LIST);

        double minDistance = 0xffff;
        Entity found = null;
        for (Entity entity : list) {
            Vec3d displacement = entity.getPositionVector().subtract(origin);
            double unmodifiedDistance = MathHelper.clamp(ray.dotProduct(displacement), 0, len);
            double distance = displacement.subtract(ray.scale(unmodifiedDistance)).length();

            if (distance < minDistance) {
                minDistance = distance;
                found =  entity;
            }
        }

        if (found == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        return found;
    }


    @Override
    public Class<?> getEvaluationType() {
        return Entity.class;
    }
}
