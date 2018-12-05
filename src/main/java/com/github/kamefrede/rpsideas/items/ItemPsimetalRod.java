package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.items.base.IPsiAddonTool;
import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;

import javax.annotation.Nullable;
import java.util.List;


@Mod.EventBusSubscriber
public class ItemPsimetalRod extends ItemMod implements IPsiAddonTool {

    protected ItemPsimetalRod() {
        super(LibItems.PSIMETAL_ROD);
        setCreativeTab(RPSCreativeTab.INST);
        setMaxStackSize(1);
        setMaxDamage(900);
        addPropertyOverride(new ResourceLocation(Reference.MODID, "cast"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {
                    boolean flag = entityIn.getHeldItemMainhand() == stack;
                    boolean flag1 = entityIn.getHeldItemOffhand() == stack;

                    if (entityIn.getHeldItemMainhand().getItem() instanceof ItemFishingRod)
                    {
                        flag1 = false;
                    }

                    return (flag || flag1) && entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).fishEntity != null ? 1.0F : 0.0F;
                }
            }
        });
    }
    private static final String TAG_REGEN_TIME = "regenTime";



    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }



    @SideOnly(Side.CLIENT)
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }


    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        return Items.FISHING_ROD.onItemRightClick(worldIn, playerIn, handIn);
    }



    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        regen(stack, entityIn, isSelected);
    }

    public static void regen(ItemStack stack, Entity entityIn, boolean isSelected) {
        if(entityIn instanceof EntityPlayer && stack.getItemDamage() > 0 && !isSelected) {
            EntityPlayer player = (EntityPlayer) entityIn;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            int regenTime = ItemNBTHelper.getInt(stack, TAG_REGEN_TIME, 0);

            if(!data.overflowed && regenTime % 80 == 0 && (float) data.getAvailablePsi() / (float) data.getTotalPsi() > 0.5F) {
                data.deductPsi(600, 5, true);
                stack.setItemDamage(stack.getItemDamage() - 1);
            }
            ItemNBTHelper.setInt(stack, TAG_REGEN_TIME, regenTime + 1);
        }
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
    public int getItemEnchantability()
    {
        return 1;
    }

    @Override
    public String getModNamespace() {
        return Reference.MODID;
    }




}
