package com.github.kamefrede.rpsideas.spells.trick.block;

import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.client.core.handler.HUDHandler;
import vazkii.psi.common.block.base.ModBlocks;

import static vazkii.psi.common.spell.trick.block.PieceTrickPlaceBlock.removeFromInventory;

public class PieceTrickDirectionPlaceBlock extends PieceTrick {

    SpellParam position;
    SpellParam direction;

    public PieceTrickDirectionPlaceBlock(Spell spell){
        super(spell);
    }

    @Override
    public void initParams(){
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(direction = new ParamVector("psi.spellparam.direction", SpellParam.GREEN, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException{
        super.addToMetadata(meta);

        meta.addStat(EnumSpellStat.POTENCY, 8);
        meta.addStat(EnumSpellStat.COST, 8);
    }

    @Override
    public Object execute(SpellContext context) throws  SpellRuntimeException{
        Vector3 positionVal = this.<Vector3>getParamValue(context, position);
        Vector3 directionVal = this.<Vector3>getParamValue(context, direction);

        if(positionVal == null ){
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        if(directionVal == null || directionVal.isZero()){
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        if(!directionVal.isAxial()) throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);

        if(!context.isInRadius(positionVal)){
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        }

        BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);

        float vectorZ = (float)directionVal.z;
        float vectorY = (float)directionVal.y;
        float vectorX = (float)directionVal.x;
        EnumFacing facing = EnumFacing.getFacingFromVector(vectorX, vectorY, vectorZ);

        placeBlock(context.caster, context.caster.getEntityWorld(), pos, context.getTargetSlot(), false, false,facing);

        return null;
    }


    public static void placeBlock(EntityPlayer player, World world, BlockPos pos, int slot, boolean particles){
        placeBlock(player, world, pos, slot, false);
    }

    public static void placeBlock(EntityPlayer player, World world, BlockPos pos, int slot, boolean particles, boolean conjure, EnumFacing facing){
        if(!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos)){
            return;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();


        if(block == null || block.isAir(state, world, pos) || block.isReplaceable(world, pos)){
            if(conjure){
                if(!world.isRemote){
                    world.setBlockState(pos, ModBlocks.conjured.getDefaultState());
                }
            } else {
                ItemStack stack = player.inventory.getStackInSlot(slot);
                if(!stack.isEmpty() && stack.getItem() instanceof ItemBlock){
                    ItemStack rem = removeFromInventory(player, block, stack);
                    ItemBlock iblock = (ItemBlock) rem.getItem();

                    Block blockToPlace = Block.getBlockFromItem(rem.getItem());
                    if(!world.isRemote){
                        IBlockState newState = blockToPlace.getStateForPlacement(world, pos, facing, 0, 0, 0, rem.getItemDamage(), player);
                        iblock.placeBlockAt(stack, player, world, pos, facing, 0, 0, 0, newState);
                        PieceTrickRotateBlock.rotateBlock(world, pos, facing);
                    }

                    if(player.capabilities.isCreativeMode){
                        HUDHandler.setRemaining(rem, -1);
                    } else {
                        HUDHandler.setRemaining(player, rem, null);
                    }
                }
                if(particles && !world.isRemote){
                    world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
                }
            }
        }


    }
}
