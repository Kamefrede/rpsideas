package com.kamefrede.rpsideas.spells.operator.entity;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
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

    public PieceOperatorClosestToLine(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        super.initParams();
        addParam(rayStartParam = new ParamVector(SpellParams.GENERIC_NAME_RAY_START, SpellParam.RED, false, false));
        addParam(rayEndParam = new ParamVector(SpellParams.GENERIC_NAME_RAY_END, SpellParam.BLUE, false, false));
        addParam(entList = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST, SpellParam.GREEN, false, false));
//        addParam(length = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.CYAN, true, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
//        SpellHelpers.ensurePositiveAndNonzero(this, length, SpellContext.MAX_DISTANCE);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 rayStart = SpellHelpers.getVector3(this, context, rayStartParam, false, false);
        Vector3 rayEnd = SpellHelpers.getVector3(this, context, rayEndParam, false, false);

//        double maxLength = SpellHelpers.getBoundedNumber(this, context, length, SpellContext.MAX_DISTANCE);
        double maxLength = MathHelper.clamp(Math.sqrt((rayStart.x - rayEnd.x) * (rayStart.x - rayEnd.x) + (rayStart.y - rayEnd.y) * (rayStart.y - rayEnd.y) + (rayStart.z - rayEnd.z) * (rayStart.z - rayEnd.z)), 0, SpellContext.MAX_DISTANCE);

        Vec3d origin = rayStart.toVec3D();
        Vec3d rayVector = rayEnd.toVec3D().subtract(origin);
        Vec3d ray = rayVector.normalize();
        double len = rayVector.length();

        EntityListWrapper list = SpellHelpers.ensureNonnullOrEmptyList(this, context, entList);

        double minDistance = maxLength;
        Entity found = null;
        for (Entity entity : list) {
            Vec3d displacement = entity.getPositionVector().subtract(origin);
            double unmodifiedDistance = MathHelper.clamp(ray.dotProduct(displacement), 0, len);
            double distance = displacement.subtract(ray.scale(unmodifiedDistance)).length();

            if (distance > maxLength)
                continue;

            if (distance <= minDistance) {
                minDistance = distance;
                found = entity;
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
