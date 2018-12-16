package com.kamefrede.rpsideas.spells.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.Collection;

public class PieceTrickRotateBlock extends PieceTrick {

    private SpellParam position;
    private SpellParam direction;

    public PieceTrickRotateBlock(Spell spell) {
        super(spell);
    }

    // I don't like this. - Wire
    @SuppressWarnings("unchecked")
    public static void rotateBlock(World world, BlockPos pos, EnumFacing axis) {
        IBlockState state = world.getBlockState(pos);
        for (IProperty<?> prop : state.getProperties().keySet()) {
            if ((prop.getName().equalsIgnoreCase("facing") || prop.getName().equalsIgnoreCase("rotation") || prop.getName().equalsIgnoreCase("axis")) && (prop.getValueClass() == EnumFacing.class || prop.getValueClass() == BlockLog.EnumAxis.class)) {
                Block block = state.getBlock();
                if (prop.getValueClass() == BlockLog.EnumAxis.class) {

                    if (!(block instanceof BlockBed) && !(block instanceof BlockPistonExtension)) {
                        IBlockState axState;
                        IProperty<BlockLog.EnumAxis> axisProp = (IProperty<BlockLog.EnumAxis>) prop;
                        axState = state.withProperty(axisProp, BlockLog.EnumAxis.fromFacingAxis(axis.getAxis()));
                        world.setBlockState(pos, axState);
                        world.updateBlockTick(pos, axState.getBlock(), 2, -1);
                        return;

                    }
                }
                if (!(block instanceof BlockBed) && !(block instanceof BlockPistonExtension)) {
                    IBlockState newState;
                    IProperty<EnumFacing> facingProperty = (IProperty<EnumFacing>) prop;
                    Collection<EnumFacing> validFacings = facingProperty.getAllowedValues();

                    if (validFacings.contains(axis)) {
                        newState = state.withProperty(facingProperty, axis);
                        world.setBlockState(pos, newState);
                        world.updateBlockTick(pos, newState.getBlock(), 2, -1);
                        return;
                    } else newState = state;


                    world.setBlockState(pos, newState);
                    world.updateBlockTick(pos, newState.getBlock(), 2, -1);
                    return;
                }
            }
        }
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(direction = new ParamVector("psi.spellparam.direction", SpellParam.GREEN, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COMPLEXITY, 2);
        meta.addStat(EnumSpellStat.COST, 2);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Vector3 directionVal = this.<Vector3>getParamValue(context, direction);

        if (positionVal == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        if (directionVal == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        World world = context.caster.world;
        BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
        IBlockState state = world.getBlockState(pos);

        if (world.isAirBlock(pos)) return null;
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, context.caster);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled())
            return null;


        EnumFacing facing = EnumFacing.getFacingFromVector((float) directionVal.x, (float) directionVal.y, (float) directionVal.z);
        rotateBlock(world, pos, facing);
        return true;

    }
}
