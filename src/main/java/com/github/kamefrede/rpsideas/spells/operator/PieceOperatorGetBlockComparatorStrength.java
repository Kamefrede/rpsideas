package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.github.kamefrede.rpsideas.util.BlockProperties;
import net.minecraft.util.EnumFacing;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;

public class PieceOperatorGetBlockComparatorStrength extends BasePieceOperatorBlockProperties<Double> {
    SpellParam axis;

    public PieceOperatorGetBlockComparatorStrength(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        super.initParams();
        addParam(axis = new ParamVector(SpellParams.GENERIC_NAME_AXIS, SpellParam.BLUE, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    Double getData(SpellContext context, BlockProperties props) throws SpellRuntimeException {
        Vector3 ax = this.<Vector3>getParamValue(context, axis);
        EnumFacing whichWay;

        if(ax == null || ax.isZero()) {
            whichWay = EnumFacing.UP;
        } else if(!ax.isAxial()) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);
        } else {
            whichWay = EnumFacing.getFacingFromVector((float) ax.x, (float) ax.y, (float) ax.z);
        }

        return (double) props.comparatorOutput(whichWay);
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
