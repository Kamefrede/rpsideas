package xyz.kamefrede.rpsideas.spells.operator.block;

import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetBlockHardness extends PieceOperator {

    private SpellParam target;

    public PieceOperatorGetBlockHardness(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        BlockPos pos = SpellHelpers.getBlockPos(this, context, target, false, false);
        IBlockState state = context.caster.world.getBlockState(pos);
        return state.getBlockHardness(context.caster.world, pos) * 1.0D;
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
