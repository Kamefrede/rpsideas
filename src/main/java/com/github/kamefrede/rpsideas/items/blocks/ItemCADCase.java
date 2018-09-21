package com.github.kamefrede.rpsideas.items.blocks;

import com.github.kamefrede.rpsideas.Psiam;
import com.github.kamefrede.rpsideas.gui.GuiHandler;
import com.github.kamefrede.rpsideas.items.base.Item16Colors;
import com.github.kamefrede.rpsideas.tiles.TileCADCase;
import com.github.kamefrede.rpsideas.util.RPSSoundHandler;
import com.github.kamefrede.rpsideas.util.helpers.MiscHelpers;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class ItemCADCase extends Item16Colors {
    public ItemCADCase(Block block) {
        super(block);

        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if(!world.isRemote) {
            player.openGui(Psiam.INSTANCE, GuiHandler.GUI_CASE, world, 0, 0, 0);
           MiscHelpers.emitSoundFromEntity(world, player, RPSSoundHandler.CAD_CASE_OPEN);
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(facing != EnumFacing.UP) {
            return EnumActionResult.FAIL;
        }

        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    //Wat
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        target.knockBack(attacker, 1f, Math.sin(attacker.rotationYaw * Math.PI / 180d), -Math.cos(attacker.rotationYaw * Math.PI / 180d));
        MiscHelpers.emitSoundFromEntity(attacker.world, attacker, RPSSoundHandler.THWACK);
        return false;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new CaseCapabilityProvider();
    }

    static class CaseCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {
        @CapabilityInject(IItemHandler.class)
        public static final Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

        ItemStackHandler handler = new TileCADCase.CaseStackHandler();

        @Override
        @SuppressWarnings("ConstantConditions")
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing enumFacing) {
            return capability == ITEM_HANDLER_CAPABILITY;
        }

        @Nullable
        @Override
        @SuppressWarnings({"ConstantConditions", "unchecked"})
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing enumFacing) {
            if(capability == ITEM_HANDLER_CAPABILITY) return (T) handler;
            else return null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return handler.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            handler.deserializeNBT(nbt);
        }
    }
}
