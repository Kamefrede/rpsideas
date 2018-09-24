package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.github.kamefrede.rpsideas.util.BlockProperties;
import net.minecraft.util.EnumFacing;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;



public class PieceOperatorGetBlockSolidity extends BasePieceOperatorBlockProperties<Double> {
    public PieceOperatorGetBlockSolidity(Spell spell) {
        super(spell);
    }

    private SpellParam axisParam;

    @Override
    public void initParams() {
        super.initParams();

        axisParam = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.BLUE, false, false);
        addParam(axisParam);
    }

    @Override
    Double getData(SpellContext context, BlockProperties props) throws SpellRuntimeException {
        Vector3 axis = getParamValue(context, axisParam);
        if(!axis.isAxial()) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);
        }

        EnumFacing facing = EnumFacing.getFacingFromVector((float) axis.x, (float) axis.y, (float) axis.z);
        return props.sideSolid(facing) ? 1d : 0d;
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
