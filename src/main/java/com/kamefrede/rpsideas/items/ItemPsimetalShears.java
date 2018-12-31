package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.items.base.IPsiAddonTool;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

import javax.annotation.Nonnull;
import java.util.List;

import static vazkii.psi.common.item.tool.ItemPsimetalTool.raytraceFromEntity;

public class ItemPsimetalShears extends ItemMod implements IPsiAddonTool {
    public ItemPsimetalShears(String name) {
        super(name);
        setMaxStackSize(1);
        setMaxDamage(900);
    }

    private boolean castIf(boolean input, ItemStack itemstack, EntityPlayer player) {
        if (input) {
            PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);

            if (data != null && !playerCad.isEmpty()) {
                ItemStack bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack));
                ItemCAD.cast(player.world, player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                    context.tool = itemstack;
                    context.positionBroken = raytraceFromEntity(player.world, player, false, player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue());
                });
            }
        }

        return input;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return Items.SHEARS.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {
        return Items.SHEARS.canHarvestBlock(blockIn);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemstack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
        return castIf(Items.SHEARS.itemInteractionForEntity(itemstack, player, entity, hand), itemstack, player);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return castIf(Items.SHEARS.onBlockStartBreak(itemstack, pos, player), itemstack, player);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return Items.SHEARS.getDestroySpeed(stack, state);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        ItemPsimetalHoe.regenPsi(stack, entityIn, isSelected);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        String componentName = TooltipHelper.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
        TooltipHelper.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repairItem) {
        return OreDictionary.containsMatch(false, OreDictionary.getOres("ingotPsi"), repairItem) || super.getIsRepairable(toRepair, repairItem);
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }
}
