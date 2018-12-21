package com.kamefrede.rpsideas.items.blocks;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.blocks.BlockCADCase;
import com.kamefrede.rpsideas.gui.GuiHandler;
import com.kamefrede.rpsideas.items.base.ProxiedItemStackHandler;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import com.kamefrede.rpsideas.util.RPSSoundHandler;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.arl.item.ItemModBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ItemCADCase extends ItemModBlock {

    public ItemCADCase(BlockCADCase block, ResourceLocation res) {
        super(block, res);

        setMaxStackSize(1);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (!world.isRemote) {
            player.openGui(RPSIdeas.INSTANCE, GuiHandler.GUI_CASE, world, 0, 0, 0);
            SpellHelpers.emitSoundFromEntity(world, player, RPSSoundHandler.CAD_CASE_OPEN);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (facing != EnumFacing.UP)
            return EnumActionResult.FAIL;

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.knockBack(attacker, 1f, Math.sin(attacker.rotationYaw * Math.PI / 180d), -Math.cos(attacker.rotationYaw * Math.PI / 180d));
        SpellHelpers.emitSoundFromEntity(attacker.world, attacker, RPSSoundHandler.THWACK);
        return false;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CaseItemHandler(stack);
    }

    public static class CaseItemHandler extends ProxiedItemStackHandler {
        public CaseItemHandler(ItemStack stack) {
            super(stack, 2);
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return TileCADCase.isAllowed(slot, stack.getItem()) ? 1 : 0;
        }
    }
}
