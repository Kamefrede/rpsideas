package com.github.kamefrede.rpsideas.spells.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickFreezeBlock extends PieceTrick {

    SpellParam position;

    public PieceTrickFreezeBlock(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 2);
        meta.addStat(EnumSpellStat.COST, 20);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.getEntityWorld().isRemote)
            return null;
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);

        if (positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        World world = context.caster.world;
        BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
        BlockPos pos1 = pos.up();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        freezeBlock(world, pos, pos1, state, block);
        return null;
    }

    static void freezeBlock(World world, BlockPos pos, BlockPos pos1, IBlockState state, Block block) {
        if(block instanceof BlockStaticLiquid && block == Blocks.WATER){
            world.setBlockState(pos, Blocks.ICE.getDefaultState());
        }
        else if(block instanceof BlockStaticLiquid && block == Blocks.LAVA){
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
        } else if(block instanceof BlockDynamicLiquid && block == Blocks.FLOWING_LAVA){
            world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
        }
        else{
            if(world.getBlockState(pos1).getBlock() == Blocks.AIR || world.getBlockState(pos1).getBlock().isReplaceable(world, pos1)){
                world.setBlockState(pos, Blocks.SNOW_LAYER.getDefaultState());
            }
        }

    }
}
