package com.github.kamefrede.rpsideas.spells.trick;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickRotateBlock extends PieceTrick {

    SpellParam position;
    SpellParam direction;

    public PieceTrickRotateBlock(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams(){
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
        if(context.caster.world.isRemote) return null;
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Vector3 directionVal = this.<Vector3>getParamValue(context, direction);

        if(positionVal == null ){
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        if(directionVal == null){
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        World world = context.caster.world;
        BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if(world.isAirBlock(pos) || !world.isBlockModifiable(context.caster, pos)) return null;
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, context.caster);
        MinecraftForge.EVENT_BUS.post(event);

        if(event.isCanceled())
            return null;

        IBlockState newState = block.getStateForPlacement(world, pos, EnumFacing.getFacingFromVector((float)directionVal.x, (float)directionVal.y, (float)directionVal.z), 0, 0, 0, block.getMetaFromState(state), context.caster);
        world.setBlockState(pos, newState);

        return true;

    }
}
