package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
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
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.client.core.handler.HUDHandler;

import static vazkii.psi.common.spell.trick.block.PieceTrickPlaceBlock.removeFromInventory;

public class PieceTrickPlant extends PieceTrick {

    private SpellParam position;

    public PieceTrickPlant(Spell spell) {
        super(spell);
    }

    public static void plantPlant(EntityPlayer player, World world, BlockPos pos, int slot) {
        if (!world.isBlockLoaded(pos) || !world.isBlockModifiable(player, pos))
            return;

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        BlockPos plantPos = pos.up();
        IBlockState stateAtPlant = world.getBlockState(plantPos);
        Block blockAtPlant = stateAtPlant.getBlock();
        ItemStack stack = player.inventory.getStackInSlot(slot);
        if (!stack.isEmpty()) {
            if (blockAtPlant.isReplaceable(world, pos)) {
                if (stack.getItem() instanceof IPlantable) {
                    ItemStack rem = removeFromInventory(player, stack);
                    IPlantable plant = ((IPlantable) rem.getItem());
                    plant(player, world, pos, plantPos, state, block, rem, plant);
                } else if (stack.getItem() instanceof ItemBlockSpecial) {
                    if (((ItemBlockSpecial) stack.getItem()).getBlock() instanceof IPlantable) {
                        ItemStack rem = removeFromInventory(player, stack);
                        IPlantable plant = ((IPlantable) ((ItemBlockSpecial) rem.getItem()).getBlock());
                        plant(player, world, pos, plantPos, state, block, rem, plant);
                    }
                } else if (stack.getItem() instanceof ItemBlock) {
                    if (((ItemBlock) stack.getItem()).getBlock() instanceof IPlantable) {
                        ItemStack rem = removeFromInventory(player, stack);
                        IPlantable plant = ((IPlantable) ((ItemBlock) rem.getItem()).getBlock());
                        plant(player, world, pos, plantPos, state, block, rem, plant);
                    }
                }

            }
        }
    }

    public static void plant(EntityPlayer player, World world, BlockPos pos, BlockPos plantPos, IBlockState state, Block block, ItemStack rem, IPlantable plant) {
        if (block.canSustainPlant(state, world, pos, EnumFacing.UP, plant)) {
            world.setBlockState(plantPos, plant.getPlant(world, pos));
            if (player.capabilities.isCreativeMode) HUDHandler.setRemaining(rem, -1);
            else HUDHandler.setRemaining(player, rem, null);

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
        BlockPos pos = SpellHelpers.getBlockPos(this, context, position, true, false);

        plantPlant(context.caster, context.caster.world, pos, context.getTargetSlot());

        return null;
    }
}
