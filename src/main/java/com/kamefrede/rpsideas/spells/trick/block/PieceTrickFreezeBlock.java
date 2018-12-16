package com.kamefrede.rpsideas.spells.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickFreezeBlock extends PieceTrick {

    private SpellParam position;

    public PieceTrickFreezeBlock(Spell spell) {
        super(spell);
    }

    private static void freezeBlock(World world, BlockPos pos) {
        boolean doesWaterVaporize = world.provider.doesWaterVaporize();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof BlockStaticLiquid && block == Blocks.WATER)
            world.setBlockState(pos, Blocks.ICE.getDefaultState());
        else if (block instanceof BlockStaticLiquid && block == Blocks.LAVA)
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
        else if (block instanceof BlockDynamicLiquid && block == Blocks.FLOWING_LAVA)
            world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
        else if (world.getBlockState(pos).getBlock() instanceof BlockSnow) {
            PropertyInteger layers = BlockSnow.LAYERS;
            int layer = state.getValue(layers);
            if (layer < 8) {
                world.setBlockState(pos, state.cycleProperty(layers));
            }

        } else {
            BlockPos up = pos.up();
            IBlockState stateAbove = world.getBlockState(up);
            Block blockAbove = stateAbove.getBlock();

            if (blockAbove.isReplaceable(world, up) && !doesWaterVaporize && state.getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID)
                world.setBlockState(up, Blocks.SNOW_LAYER.getDefaultState());
        }

    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 9);
        meta.addStat(EnumSpellStat.COST, 30);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.getEntityWorld().isRemote)
            return null;
        Vector3 pos = this.getParamValue(context, position);

        if (pos == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (!context.isInRadius(pos))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        World world = context.caster.world;
        freezeBlock(world, pos.toBlockPos());
        return null;
    }


}
