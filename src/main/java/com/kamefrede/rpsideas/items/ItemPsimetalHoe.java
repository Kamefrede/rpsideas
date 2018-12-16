package com.kamefrede.rpsideas.items;

import com.google.common.collect.Multimap;
import com.kamefrede.rpsideas.items.base.RPSItem;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemPsimetalHoe extends RPSItem implements IPsimetalTool {
    private static final String TAG_REGEN_TIME = "regenTime";
    private final float speed;

    public ItemPsimetalHoe(String name) {
        super(name);
        setCreativeTab(RPSCreativeTab.INSTANCE);
        this.speed = 4.0F;
        this.setMaxDamage(900);

    }

    public static void regenPsi(ItemStack stack, Entity entityIn, boolean isSelected) {
        if (entityIn instanceof EntityPlayer && stack.getItemDamage() > 0 && !isSelected) {
            EntityPlayer player = (EntityPlayer) entityIn;
            PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
            int regenTime = ItemNBTHelper.getInt(stack, TAG_REGEN_TIME, 0);

            if (data != null && !data.overflowed && regenTime % 80 == 0 && (float) data.getAvailablePsi() / (float) data.getTotalPsi() > 0.5F) {
                data.deductPsi(600, 5, true);
                stack.setItemDamage(stack.getItemDamage() - 1);
            }
            ItemNBTHelper.setInt(stack, TAG_REGEN_TIME, regenTime + 1);
        }
    }
    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase attacker) {
        itemstack.damageItem(1, attacker);
        return true;
    }


    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (Items.IRON_HOE.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
            PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);
            if (data != null && !playerCad.isEmpty()) {
                ItemStack bullet = getBulletInSocket(stack, getSelectedSlot(stack));
                ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                    context.tool = stack;
                    context.positionBroken = new RayTraceResult(new Vec3d(hitX + pos.getX(), hitY + pos.getY(), hitZ + pos.getZ()), facing, pos);
                });
            }
            Block block = worldIn.getBlockState(pos).getBlock();
            worldIn.notifyNeighborsOfStateChange(pos, block, true);
            return Items.IRON_HOE.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        return EnumActionResult.PASS;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D() {
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        regenPsi(stack, entityIn, isSelected);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
        ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repairItem) {
        return OreDictionary.containsMatch(false, OreDictionary.getOres("ingotPsi"), repairItem) || super.getIsRepairable(toRepair, repairItem);
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }

    @Nonnull
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(@Nonnull EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) (this.speed - 4.0F), 0));
        }

        return multimap;
    }
}
