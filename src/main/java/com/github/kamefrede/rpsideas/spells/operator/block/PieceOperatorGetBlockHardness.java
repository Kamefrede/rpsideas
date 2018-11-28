package com.github.kamefrede.rpsideas.spells.operator.block;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.github.kamefrede.rpsideas.util.BlockProperties;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetBlockHardness extends PieceOperator {

    public PieceOperatorGetBlockHardness(Spell spell){
        super(spell);
    }

    SpellParam target;

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 vec = this.<Vector3>getParamValue(context, target);
        if(vec == null || vec.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
        IBlockState state = context.caster.world.getBlockState(pos);
        return state.getBlockHardness(context.caster.world, pos) * 1.0D;
    }

    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
