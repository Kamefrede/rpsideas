package com.kamefrede.rpsideas.items.flow;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.render.elytra.ICustomElytra;
import com.kamefrede.rpsideas.render.elytra.ModelCustomElytra;
import com.kamefrede.rpsideas.render.elytra.ModelElytraChestpiece;
import com.kamefrede.rpsideas.render.elytra.ModelPsiElytra;
import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.common.item.tool.IPsimetalTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 * Created at 10:57 AM on 4/3/19.
 */
public class ItemPsiElytra extends ItemMod implements ICustomElytra {
    public ItemPsiElytra(String name, int damage) {
        super(name);
        setMaxStackSize(1);
        setMaxDamage(damage);
        this.addPropertyOverride(new ResourceLocation("broken"),
                (stack, worldIn, entityIn) -> ItemElytra.isUsable(stack) ? 0 : 1);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return IPsimetalTool.isRepairableBy(repair);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack held = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot target = EntityLiving.getSlotForItemStack(held);
        ItemStack equipped = playerIn.getItemStackFromSlot(target);

        if (equipped.isEmpty()) {
            playerIn.setItemStackToSlot(target, held.copy());
            held.setCount(0);
            return new ActionResult<>(EnumActionResult.SUCCESS, held);
        } else
            return new ActionResult<>(EnumActionResult.FAIL, held);
    }


    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        IPsimetalTool.regen(stack, entityIn, isSelected);
    }

    @Nullable
    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EntityEquipmentSlot.CHEST;
    }

    @SideOnly(Side.CLIENT)
    private static ModelCustomElytra elytraModel;
    @SideOnly(Side.CLIENT)
    private static ModelElytraChestpiece chestpieceModel;

    private static final ResourceLocation ELYTRA_TEXTURE = new ResourceLocation(RPSIdeas.MODID, "textures/model/psi_elytra.png");

    @Override
    public ResourceLocation getElytraTexture(ItemStack stack) {
        return ELYTRA_TEXTURE;
    }

    @Override
    public ModelCustomElytra getElytraModel(ItemStack stack) {
        if (elytraModel == null)
            elytraModel = new ModelPsiElytra();
        return elytraModel;
    }

    @Nullable
    @Override
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (chestpieceModel == null)
            chestpieceModel = new ModelElytraChestpiece();
        return chestpieceModel;
    }
}
