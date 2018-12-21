package com.kamefrede.rpsideas.spells.operator.entity;

import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class OperatorDistanceFromGround extends PieceOperator {

    private SpellParam target;

    public OperatorDistanceFromGround(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 targetVal = this.getParamValue(context, target);
        if (targetVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        World world = context.caster.getEntityWorld();
        BlockPos pos = targetVal.toBlockPos();

        Chunk chunk = world.getChunk(pos);
        BlockPos topFound = new BlockPos(pos.getX(), Math.min(chunk.getTopFilledSegment() + 16, pos.getY()), pos.getZ());
        BlockPos pointer;

        while (topFound.getY() > 0) {
            pointer = topFound.down();
            IBlockState state = chunk.getBlockState(pointer);

            if (state.getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID)
                break;

            topFound = pointer;
        }

        if (topFound.getY() < 0)
            throw new SpellRuntimeException(SpellRuntimeExceptions.OUT_OF_BOUNDS);

        return targetVal.y - (topFound.getY() + 1);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
