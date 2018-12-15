package com.kamefrede.rpsideas.items;

import com.google.common.collect.Multimap;
import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.IPsimetalTool;

import java.util.List;

import static vazkii.psi.common.item.ItemCAD.cast;

public class ItemPsionicHoe extends ItemMod implements IPsimetalTool { // TODO: 12/15/18 look at
    private static final String TAG_REGEN_TIME = "regenTime";
    public static String[] variants = {LibItemNames.PSIMETAL_HOE, LibItemNames.IVORY_HOE, LibItemNames.EBONY_HOE};
    private final float speed;
    protected Item.ToolMaterial toolMaterial;

    protected ItemPsionicHoe(String name) {
        super(name);
        setCreativeTab(RPSCreativeTab.INSTANCE);
        this.speed = 4.0F;
        this.setMaxDamage(900);

    }

    public static void regen(ItemStack stack, Entity entityIn, boolean isSelected) {
        if (entityIn instanceof EntityPlayer && stack.getItemDamage() > 0 && !isSelected) {
            EntityPlayer player = (EntityPlayer) entityIn;
            PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
            int regenTime = ItemNBTHelper.getInt(stack, TAG_REGEN_TIME, 0);

            if (!data.overflowed && regenTime % 80 == 0 && (float) data.getAvailablePsi() / (float) data.getTotalPsi() > 0.5F) {
                data.deductPsi(600, 5, true);
                stack.setItemDamage(stack.getItemDamage() - 1);
            }
            ItemNBTHelper.setInt(stack, TAG_REGEN_TIME, regenTime + 1);
        }
    }

    public static RayTraceResult raytraceFromEntity(World world, Entity player, boolean par3, double range) {
        float f = 1.0F;
        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double d0 = player.prevPosX + (player.posX - player.prevPosX) * f;
        double d1 = player.prevPosY + (player.posY - player.prevPosY) * f;
        if (player instanceof EntityPlayer)
            d1 += ((EntityPlayer) player).eyeHeight;
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * f;
        Vec3d vec3 = new Vec3d(d0, d1, d2);
        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
        float f6 = MathHelper.sin(-f1 * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;
        double d3 = range;
        Vec3d vec31 = vec3.add(f7 * d3, f6 * d3, f8 * d3);
        return world.rayTraceBlocks(vec3, vec31, par3);
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, EntityLivingBase target, EntityLivingBase attacker) {
        super.hitEntity(itemstack, target, attacker);

        if (attacker instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) attacker;

            PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);

            if (!playerCad.isEmpty()) {
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
    public String getModNamespace() {
        return RPSIdeas.MODID;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (Items.IRON_HOE.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS) {
            PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(player);
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
    public boolean isFull3D() {
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        regen(stack, entityIn, isSelected);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        String componentName = ItemMod.local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
        ItemMod.addToTooltip(tooltip, "psimisc.spellSelected", componentName);
    }

    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return par2ItemStack.getItem() == ModItems.material && par2ItemStack.getItemDamage() == 1 || super.getIsRepairable(par1ItemStack, par2ItemStack);
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return false;
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 0.0D, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", (double) (this.speed - 4.0F), 0));
        }

        return multimap;
    }
}
