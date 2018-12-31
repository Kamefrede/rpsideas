package com.kamefrede.rpsideas.spells.operator.block;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetBlockLight extends PieceOperator {

    private SpellParam target;

    public PieceOperatorGetBlockLight(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        BlockPos pos = SpellHelpers.getBlockPos(this, context, target);
        IBlockState state = context.caster.world.getBlockState(pos);
        return state.getLightValue(context.caster.world, pos) * 1.0;
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
