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

public class PieceOperatorGetMiningLevel extends PieceOperator {

    private SpellParam position;

    public PieceOperatorGetMiningLevel(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, true, false);
        IBlockState state = context.caster.world.getBlockState(pos);
        return state.getBlock().getHarvestLevel(state) * 1.0D;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
