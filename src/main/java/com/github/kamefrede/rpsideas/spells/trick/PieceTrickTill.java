package com.github.kamefrede.rpsideas.spells.trick;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickTill extends PieceTrick {

    SpellParam position;

    public PieceTrickTill(Spell spell){
        super(spell);
    }

    @Override
    public void initParams(){
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException{
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COST, 10);
        meta.addStat(EnumSpellStat.POTENCY, 10);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException{

        Vector3 positionVal = this.<Vector3>getParamValue(context, position);

        if(positionVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if(!context.isInRadius(positionVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
        tillBlock(context.caster, context.caster.getEntityWorld(), pos);

        return null;

    }

    public static void tillBlock(EntityPlayer player, World world, BlockPos pos){
        if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos)) {
            return;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.GRASS || block == Blocks.GRASS_PATH)
        {
            world.setBlockState(pos, Blocks.FARMLAND.getDefaultState());
        }

        if (block == Blocks.DIRT){
            if(state.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.COARSE_DIRT){
                world.setBlockState(pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
            } else {
                world.setBlockState(pos, Blocks.FARMLAND.getDefaultState());
            }
        }



    }


}

