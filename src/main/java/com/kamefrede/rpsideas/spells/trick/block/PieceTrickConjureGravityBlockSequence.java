package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import static com.kamefrede.rpsideas.spells.trick.block.PieceTrickConjureEtherealBlockSequence.addBlocksVal;

public class PieceTrickConjureGravityBlockSequence extends PieceTrick {

    private SpellParam position;
    private SpellParam target;
    private SpellParam maxBlocks;
    private SpellParam time;

    public PieceTrickConjureGravityBlockSequence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
        addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.PURPLE, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        addBlocksVal(this, maxBlocks, meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = SpellHelpers.getVector3(this, context, position, true, false);
        Vector3 targetVal = SpellHelpers.getVector3(this, context, target, false, false);
        double maxBlocksVal = SpellHelpers.getNumber(this, context, maxBlocks, 0);
        Double timeVal = this.getParamValue(context, time);

        int len = (int) targetVal.mag();
        Vector3 targetNorm = targetVal.copy().normalize();

        for (int i = 0; i < Math.min(len, maxBlocksVal); i++) {
            Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));


            BlockPos pos = blockVec.toBlockPos();
            SpellHelpers.isBlockPosInRadius(context, pos);
            if (!context.caster.world.isBlockModifiable(context.caster, pos))
                continue;

            IBlockState state = context.caster.world.getBlockState(pos);

            if (state.getBlock() != RPSBlocks.conjuredGravityBlock) {
                PieceTrickConjureGravityBlock.placeBlock(context.caster, context.caster.world, pos, true);
                state = context.caster.world.getBlockState(pos);

                if (state.getBlock() == RPSBlocks.conjuredGravityBlock)
                    PieceTrickConjureEtherealBlock.setColorAndTime(context, timeVal, pos, state);
            }
        }

        return null;
    }
}
