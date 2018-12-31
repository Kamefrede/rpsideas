package com.kamefrede.rpsideas.spells.operator.block;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;


public class PieceOperatorGetBlockSolidity extends PieceOperator {

    private SpellParam axisParam;
    private SpellParam target;

    public PieceOperatorGetBlockSolidity(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
        addParam(axisParam = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        BlockPos pos = SpellHelpers.getBlockPos(this, context, target);
        Vector3 axis = this.getParamValue(context, axisParam);
        if (axis == null || !axis.isAxial() || axis.isZero())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);

        IBlockState state = context.caster.world.getBlockState(pos);
        return state.isSideSolid(context.caster.world, pos, EnumFacing.getFacingFromVector((float) axis.x, (float) axis.y, (float) axis.z)) ? 1.0D : 0.D;
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
