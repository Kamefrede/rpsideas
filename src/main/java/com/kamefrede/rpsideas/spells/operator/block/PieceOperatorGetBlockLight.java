package com.kamefrede.rpsideas.spells.operator.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetBlockLight extends PieceOperator {// TODO: 12/15/18 look at

    SpellParam target;

    public PieceOperatorGetBlockLight(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 vec = this.<Vector3>getParamValue(context, target);
        if (vec == null || vec.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
        IBlockState state = context.caster.world.getBlockState(pos);
        return state.getLightValue(context.caster.world, pos) * 1.0D;
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
