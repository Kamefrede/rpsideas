package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.github.kamefrede.rpsideas.util.BlockProperties;
import net.minecraft.util.EnumFacing;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;



public class PieceOperatorGetBlockSolidity extends BasePieceOperatorBlockProperties<Double> {

    SpellParam axis;

    public PieceOperatorGetBlockSolidity(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(axis = new ParamVector(SpellParams.GENERIC_NAME_AXIS, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    Double getData(SpellContext context, BlockProperties props) throws SpellRuntimeException {
        Vector3 ax = this.<Vector3>getParamValue(context, axis);
        if(!ax.isAxial()){
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);
        }

        EnumFacing facing = EnumFacing.getFacingFromVector((float) ax.x, (float) ax.y, (float) ax.z);
        return props.sideSolid(facing) ? 1d : 0d;
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
