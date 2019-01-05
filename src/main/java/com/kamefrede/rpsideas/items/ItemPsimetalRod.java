package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.IPsiAddonTool;
import com.kamefrede.rpsideas.items.base.ItemModRod;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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
import java.util.Objects;


@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemPsimetalRod extends ItemModRod implements IPsiAddonTool {

    protected ItemPsimetalRod(String name) {
        super(name);
        setMaxStackSize(1);
        setMaxDamage(900);
    }

    public static void castSpell(EntityPlayer player, ItemStack stack, Vec3d pos) {
        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
        ItemStack playerCad = PsiAPI.getPlayerCAD(player);
        if (stack.getItem() instanceof ItemPsimetalRod) {
            ItemPsimetalRod rod = (ItemPsimetalRod) stack.getItem();
            if (data != null && !playerCad.isEmpty()) {
                ItemStack bullet = rod.getBulletInSocket(stack, rod.getSelectedSlot(stack));
                ItemCAD.cast(player.world, player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                    context.tool = stack;
                    context.positionBroken = new RayTraceResult(pos, EnumFacing.UP);
                });
            }
        }

    }

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.inventory.getCurrentItem();
        Vec3d pos = new Vec3d(event.getHookEntity().posX, event.getHookEntity().posY, event.getHookEntity().posZ);
        castSpell(player, stack, pos);
    }


    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.fishEntity != null) {
            if (playerIn.fishEntity.caughtEntity instanceof EntityLivingBase) {
                PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(playerIn);
                ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);

                if (data != null && !playerCad.isEmpty()) {
                    ItemStack bullet = getBulletInSocket(itemstack, getSelectedSlot(itemstack));
                    ItemCAD.cast(playerIn.world, playerIn, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                        context.tool = itemstack;
                        context.attackedEntity = (EntityLivingBase) playerIn.fishEntity.caughtEntity;
                    });
                }
            }
            int i = playerIn.fishEntity.handleHookRetraction();
            itemstack.damageItem(i, playerIn);
            playerIn.swingArm(handIn);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        } else {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote) {
                EntityFishHook entityfishhook = new EntityFishHook(worldIn, playerIn);
                int lure = EnchantmentHelper.getFishingSpeedBonus(itemstack);

                if (lure > 0) entityfishhook.setLureSpeed(lure);

                int luck = EnchantmentHelper.getFishingLuckBonus(itemstack);

                if (luck > 0) entityfishhook.setLuck(luck);

                worldIn.spawnEntity(entityfishhook);
            }

            playerIn.swingArm(handIn);
            playerIn.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        ItemPsimetalHoe.regenPsi(stack, entityIn, isSelected);
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

    public int getItemEnchantability() {
        return 1;
    }


}
