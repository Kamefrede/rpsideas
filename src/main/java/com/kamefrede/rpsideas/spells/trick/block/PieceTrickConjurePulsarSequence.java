package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.blocks.BlockConjuredPulsar;
import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickConjurePulsarSequence extends PieceTrick {

    private SpellParam positionParam;
    private SpellParam targetParam;
    private SpellParam maxBlocksParam;
    private SpellParam timeParam;
    public PieceTrickConjurePulsarSequence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        positionParam = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false);
        targetParam = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false);
        maxBlocksParam = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true);
        timeParam = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false);
        SpellHelpers.addAllParams(this, positionParam, targetParam, maxBlocksParam, timeParam);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        double maxBlocksValue = SpellHelpers.ensurePositiveAndNonzero(this, maxBlocksParam);

        meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksValue * 20));
        meta.addStat(EnumSpellStat.COST, (int) (maxBlocksValue * 30));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;

        Vector3 positionVec = getParamValue(context, positionParam);
        Vector3 targetVec = getParamValue(context, targetParam);
        int maxBlocks = (int) SpellHelpers.getNumber(this, context, maxBlocksParam, 0);
        int time = (int) SpellHelpers.getNumber(this, context, timeParam, 0);

        if (positionVec == null || targetVec == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        int length = (int) Math.min(targetVec.mag(), maxBlocks);
        Vector3 normalizedDirection = targetVec.copy().normalize();

        for (int i = 0; i < length; i++) {
            Vector3 blockVector = positionVec.copy().add(normalizedDirection.copy().multiply(i));
            if (!context.isInRadius(blockVector)) {
                throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
            }

            World world = context.caster.world;
            BlockPos pos = blockVector.toBlockPos();
            IBlockState state = getStateToSet();

            if (SpellHelpers.placeBlock(world, pos, state, false))
                PieceTrickConjureEtherealBlock.setColorAndTime(context, (double) time, pos, state);
        }

        return null;
    }

    protected IBlockState getStateToSet() {
        return RPSBlocks.conjuredPulsar.getDefaultState().withProperty(BlockConjuredPulsar.SOLID, true);
    }
}
