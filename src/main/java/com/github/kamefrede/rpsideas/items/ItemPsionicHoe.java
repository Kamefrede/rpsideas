package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemModTool;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.ItemPsimetalTool;

import java.util.Set;

import static vazkii.psi.common.item.ItemCAD.cast;
import static vazkii.psi.common.item.ItemCAD.craft;

public class ItemPsionicHoe extends ItemPsimetalTool {
    protected ItemPsionicHoe() {
        super(LibItems.PSIMETAL_HOE, -2.0F, 0F, EFFECTIVE_ON);
        setCreativeTab(RPSCreativeTab.INST);
        setHarvestLevel("hoe", 3);
    }

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet();

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase attacker) {
        super.hitEntity(itemstack, target, attacker);

        if(attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;

            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);

            if(!playerCad.isEmpty()) {
                ItemStack bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack));
                cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                    context.attackedEntity = target;
                });
            }
        }

        itemstack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return false;
    }


    @Override
    public String getModNamespace() {
        return Reference.MODID;
    }


    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (Items.IRON_HOE.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);
            if (!playerCad.isEmpty()) {
                ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
                ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                    context.tool = stack;
                    context.positionBroken = raytraceFromEntity(player.getEntityWorld(), player, false, player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE).getAttributeValue());
                });
            }
            Block block = worldIn.getBlockState(pos).getBlock();
            worldIn.notifyNeighborsOfStateChange(pos, block, true);
            return Items.IRON_HOE.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }



}
