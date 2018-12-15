package com.kamefrede.rpsideas.spells.trick.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.client.core.handler.HUDHandler;

import static vazkii.psi.common.spell.trick.block.PieceTrickPlaceBlock.removeFromInventory;

public class PieceTrickPlant extends PieceTrick {// TODO: 12/15/18 look at

    SpellParam position;

    public PieceTrickPlant(Spell spell) {
        super(spell);
    }

    @SuppressWarnings("Duplicates")
    public static void plantPlant(EntityPlayer player, World world, BlockPos pos, int slot, BlockPos pos2) {
        if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos)) {
            return;
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        IBlockState state2 = world.getBlockState(pos2);
        Block block2 = state2.getBlock();
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (!stack.isEmpty()) {
            if (block2 == null || block2.isAir(state, world, pos) || block2.isReplaceable(world, pos)) {
                if (stack.getItem() instanceof IPlantable) {
                    ItemStack rem = removeFromInventory(player, block, stack);
                    IPlantable plant = ((IPlantable) rem.getItem());
                    if (block.canSustainPlant(state, world, pos, EnumFacing.UP, plant)) {
                        world.setBlockState(pos2, plant.getPlant(world, pos));
                        if (player.capabilities.isCreativeMode) {
                            HUDHandler.setRemaining(rem, -1);
                        } else {
                            HUDHandler.setRemaining(player, rem, null);
                        }
                    }

                } else if (stack.getItem() instanceof ItemBlockSpecial) {
                    if (((ItemBlockSpecial) stack.getItem()).getBlock() instanceof IPlantable) {
                        ItemStack rem = removeFromInventory(player, block, stack);
                        IPlantable plant = ((IPlantable) ((ItemBlockSpecial) rem.getItem()).getBlock());
                        if (block.canSustainPlant(state, world, pos, EnumFacing.UP, plant)) {
                            world.setBlockState(pos2, plant.getPlant(world, pos));
                            if (player.capabilities.isCreativeMode) {
                                HUDHandler.setRemaining(rem, -1);
                            } else {
                                HUDHandler.setRemaining(player, rem, null);
                            }
                        }
                    }

                } else if (stack.getItem() instanceof ItemBlock) {
                    if (((ItemBlock) stack.getItem()).getBlock() instanceof IPlantable) {
                        ItemStack rem = removeFromInventory(player, block, stack);
                        IPlantable plant = ((IPlantable) ((ItemBlock) rem.getItem()).getBlock());
                        if (block.canSustainPlant(state, world, pos, EnumFacing.UP, plant)) {
                            world.setBlockState(pos2, plant.getPlant(world, pos));
                            if (player.capabilities.isCreativeMode) {
                                HUDHandler.setRemaining(rem, -1);
                            } else {
                                HUDHandler.setRemaining(player, rem, null);
                            }

                        }

                    }
                }

            }
        }
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 8);
        meta.addStat(EnumSpellStat.COST, 8);
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

        BlockPos pos = new BlockPos(positionVal.x, positionVal.y, positionVal.z);
        BlockPos plantPos = new BlockPos(positionVal.x, positionVal.y + 1, positionVal.z);

        plantPlant(context.caster, context.caster.getEntityWorld(), pos, context.getTargetSlot(), plantPos);


        return null;
    }
}
