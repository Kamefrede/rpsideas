package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickConjureGravityBlock extends PieceTrick {
    private SpellParam position;
    private SpellParam time;

    public PieceTrickConjureGravityBlock(Spell spell) {
        super(spell);
    }

    public static void placeBlock(EntityPlayer player, World world, BlockPos pos, boolean conjure) {
        if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos))
            return;

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isAir(state, world, pos) || block.isReplaceable(world, pos)) {
            if (conjure) {
                world.setBlockState(pos, RPSBlocks.conjuredGravityBlock.getDefaultState());
            }
        }
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        addStats(meta);
    }

    public void addStats(SpellMetadata meta) {
        meta.addStat(EnumSpellStat.POTENCY, 10);
        meta.addStat(EnumSpellStat.COST, 10);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);
        double timeVal = SpellHelpers.getNumber(this, context, time, -1);

        if (!context.caster.world.isBlockModifiable(context.caster, pos))
            return null;

        IBlockState state = context.caster.world.getBlockState(pos);
        if (state.getBlock() != RPSBlocks.conjuredGravityBlock) {
            placeBlock(context.caster, context.caster.world, pos, true);

            state = context.caster.world.getBlockState(pos);

            if (state.getBlock() == RPSBlocks.conjuredGravityBlock)
                PieceTrickConjureEtherealBlock.setColorAndTime(context, timeVal, pos, state);
        }

        return null;
    }
}
