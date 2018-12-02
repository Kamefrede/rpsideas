package com.github.kamefrede.rpsideas.spells.macro;

import com.github.kamefrede.rpsideas.spells.base.PieceMacro;
import net.minecraft.util.math.RayTraceResult;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.spell.operator.vector.PieceOperatorVectorRaycast;

public class PieceMacroCasterRaycast extends PieceMacro {

    SpellParam max;

    public PieceMacroCasterRaycast(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(max = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, true, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 originVal = Vector3.fromEntity(context.caster).add(0, context.caster.getEyeHeight(),0);
        Vector3 rayVal = new Vector3(context.caster.getLook(1F));


        double maxLen = SpellContext.MAX_DISTANCE;
        Double numberVal = this.<Double>getParamValue(context, max);
        if(numberVal != null)
            maxLen = numberVal.doubleValue();
        maxLen = Math.min(SpellContext.MAX_DISTANCE, maxLen);

        RayTraceResult pos = PieceOperatorVectorRaycast.raycast(context.caster.getEntityWorld(), originVal, rayVal, maxLen);
        if(pos == null || pos.getBlockPos() == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);

        return new Vector3(pos.getBlockPos().getX(), pos.getBlockPos().getY(), pos.getBlockPos().getZ());
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }


}
