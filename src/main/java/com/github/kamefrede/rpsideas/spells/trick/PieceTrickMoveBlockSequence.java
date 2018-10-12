package com.github.kamefrede.rpsideas.spells.trick;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickMoveBlockSequence extends PieceTrick {

    SpellParam position;
    SpellParam target;
    SpellParam maxBlocks;

    public PieceTrickMoveBlockSequence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.GREEN, false, false));
        addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        Double maxBlocksVal = this.<Double>getParamEvaluation(maxBlocks);
        if(maxBlocksVal == null || maxBlocksVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

        meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 10));
        meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 15));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.getEntityWorld().isRemote)
            return null;

        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Vector3 targetVal = this.<Vector3>getParamValue(context, target);
        Double maxBlocksVal = this.<Double>getParamValue(context, maxBlocks);
        int maxBlocksInt = maxBlocksVal.intValue();

        if(positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if(!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        int len = (int) targetVal.mag();
        Vector3 targetNorm = targetVal.copy().normalize();
        if(!targetVal.isAxial() || targetVal.isZero())
            return null;
        for(int i = 0; i < Math.min(len, maxBlocksInt); i++) {
            Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

            if(!context.isInRadius(blockVec))
                throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

            World world = context.caster.getEntityWorld();
            BlockPos pos = new BlockPos(blockVec.x, blockVec.y, blockVec.z);
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, context.caster);
            MinecraftForge.EVENT_BUS.post(event);

            if(event.isCanceled())
                return null;

            if(world.getTileEntity(pos) != null || block.getPushReaction(state) != EnumPushReaction.NORMAL || !block.canSilkHarvest(world, pos, state, context.caster) || block.getPlayerRelativeBlockHardness(state, context.caster, world, pos) <= 0 || !ForgeHooks.canHarvestBlock(block, context.caster, world, pos))
                return null;

            Vector3 axis = targetVal.normalize();
            int x = pos.getX() + (int) axis.x;
            int y = pos.getY() + (int) axis.y;
            int z = pos.getZ() + (int) axis.z;

            BlockPos pos1 = new BlockPos(x, y, z);
            IBlockState state1 = world.getBlockState(pos1);

            if(!world.isBlockModifiable(context.caster, pos) || !world.isBlockModifiable(context.caster, pos1))
                return null;

            if(world.isAirBlock(pos1) || state1.getBlock().isReplaceable(world, pos1)) {
                world.setBlockState(pos1, state, 1 | 2);
                world.setBlockToAir(pos);
                world.playEvent(2001, pos, Block.getIdFromBlock(block) + (block.getMetaFromState(state) << 12));
            }

            return null;
        }
        return null;
    }
}
