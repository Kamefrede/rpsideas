package com.kamefrede.rpsideas.spells.trick.block;

import com.google.common.collect.Maps;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
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

import java.util.Map;

public class PieceTrickMoveBlockSequence extends PieceTrick {

    private SpellParam position;
    private SpellParam target;
    private SpellParam maxBlocks;
    private SpellParam direction;

    public PieceTrickMoveBlockSequence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
        addParam(maxBlocks = new ParamNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.RED, false, true));
        addParam(direction = new ParamVector(SpellParams.GENERIC_NAME_DIRECTION, SpellParam.GREEN, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        double maxBlocksVal = SpellHelpers.ensurePositiveAndNonzero(this, maxBlocks);

        meta.addStat(EnumSpellStat.POTENCY, (int) (maxBlocksVal * 10));
        meta.addStat(EnumSpellStat.COST, (int) (maxBlocksVal * 15));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 directionVal = SpellHelpers.getVector3(this, context, direction, false, true);
        Vector3 positionVal = SpellHelpers.getVector3(this, context, position, true, false);
        Vector3 targetVal = SpellHelpers.getVector3(this, context, target, false, false);
        double maxBlocksVal = SpellHelpers.getNumber(this, context, maxBlocks, 0);


        Map<BlockPos, IBlockState> toSet = Maps.newHashMap();

        int len = (int) targetVal.copy().mag();

        Vector3 directNorm = directionVal.copy().normalize();
        Vector3 targetNorm = targetVal.copy().normalize();


        for (int i = 0; i < Math.min(len, maxBlocksVal); i++) {
            Vector3 blockVec = positionVal.copy().add(targetNorm.copy().multiply(i));

            World world = context.caster.world;
            BlockPos pos = blockVec.toBlockPos();
            SpellHelpers.isBlockPosInRadius(context, pos);
            IBlockState state = world.getBlockState(pos);
            Block block = state.getBlock();
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, context.caster);

            if (MinecraftForge.EVENT_BUS.post(event))
                continue;


            if (world.getTileEntity(pos) != null ||
                    state.getPushReaction() != EnumPushReaction.NORMAL ||
                    !block.canSilkHarvest(world, pos, state, context.caster) ||
                    state.getPlayerRelativeBlockHardness(context.caster, world, pos) <= 0 ||
                    !ForgeHooks.canHarvestBlock(block, context.caster, world, pos))
                continue;



            BlockPos pushToPos = pos.add(directNorm.x, directNorm.y, directNorm.z);
            BlockPos nextPos = pos.add(targetNorm.x, targetNorm.y, targetNorm.z);
            IBlockState pushToState = world.getBlockState(pushToPos);

            if (!world.isBlockModifiable(context.caster, pos) ||
                    !world.isBlockModifiable(context.caster, pushToPos))
                continue;


            if (y > 256 || y < 1) continue;


            if (world.isAirBlock(pushToPos) ||
                    (nextPos.equals(pushToPos) && i + 1 < Math.min(len, maxBlocksVal)) ||
                    pushToState.getBlock().isReplaceable(world, pushToPos)) {
                world.setBlockToAir(pos);
                world.playEvent(2001, pos, Block.getStateId(state));
                toSet.put(pushToPos, state);
            }
        }

        for (Map.Entry<BlockPos, IBlockState> pairToSet : toSet.entrySet())
            context.caster.world.setBlockState(pairToSet.getKey(), pairToSet.getValue());

        return null;
    }
}
