package com.github.kamefrede.rpsideas.spells.operator.block;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;


public class PieceOperatorGetBlockSolidity extends PieceOperator {
    public PieceOperatorGetBlockSolidity(Spell spell) {
        super(spell);
    }

    SpellParam axisParam;
    SpellParam target;

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(axisParam = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 vec = this.<Vector3>getParamValue(context, target);
        Vector3 axis = this.<Vector3>getParamValue(context, axisParam);
        if(vec == null || vec.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        if(!axis.isAxial() || axis.isZero() || axis == null) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);
        }

        BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
        IBlockState state = context.caster.world.getBlockState(pos);
        return state.isSideSolid(context.caster.world, pos, EnumFacing.getFacingFromVector((float)axis.x,(float)axis.y,(float)axis.z)) ? 1.0D : 0.D ;
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
