package com.github.kamefrede.rpsideas.items.components;

import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.items.base.ICadComponentAcceptor;
import com.github.kamefrede.rpsideas.items.base.ItemComponent;
import com.github.kamefrede.rpsideas.util.helpers.ItemNBTHelpers;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.cad.ICADComponent;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.ref.Reference;
import java.util.List;

public class ItemLiquidColorizer extends ItemComponent implements  ICADColorizer, IItemColorProvider, ICadComponentAcceptor {

    public static class Companion{
        public static int getColorFromStack(ItemStack stack){
            return ItemNBTHelper.getInt(stack, "color", Integer.MAX_VALUE);
        }

        public static ItemStack getInheriting(ItemStack stack){
            NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, "inheriting", true);
            if(cmp == null) return ItemStack.EMPTY;
            return new ItemStack(cmp);
        }

        public static ItemStack setInheriting(ItemStack stack, ItemStack inheriting){
            if(inheriting.isEmpty()){
                ItemNBTHelpers.removeTag(stack, "inheriting");
            } else{
                NBTTagCompound nbt = new NBTTagCompound();
                inheriting.writeToNBT(nbt);
                ItemNBTHelpers.setCompound(stack, "inheriting", nbt);
            }
            return stack;
        }
    }

    @Override
    public ItemStack setPiece(ItemStack stack, EnumCADComponent type, ItemStack piece) {
        if( type != EnumCADComponent.DYE)
            return stack;
        return Companion.setInheriting(stack, piece);
    }

    @Override
    public ItemStack getPiece(ItemStack stack, EnumCADComponent type) {
        if(type != EnumCADComponent.DYE)
            return ItemStack.EMPTY;
        return Companion.getInheriting(stack);
    }

    @Override
    public boolean acceptsPiece(ItemStack stack, EnumCADComponent type) {
        return type == EnumCADComponent.DYE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IItemColor getItemColor() {
        return (stack, tintindex) -> tintindex == 1 ? getColor(stack) : -1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColor(ItemStack stack) {
        int itemcolor = ItemNBTHelper.getInt(stack, "color", Integer.MAX_VALUE);
        if(!stack.isEmpty()){
            ItemStack inheriting = Companion.getInheriting(stack);
            if(!inheriting.isEmpty() && inheriting.getItem() instanceof ICADColorizer){
                int inheritcolor = ((ICADColorizer)inheriting.getItem()).getColor(inheriting);
                if(itemcolor == Integer.MAX_VALUE)
                    itemcolor = inheritcolor;
                else{
                    Color it = new Color(itemcolor);
                    Color inh = new Color(inheritcolor);
                    itemcolor = new Color((it.getRed() + inh.getRed()) / 2, (it.getGreen() + inh.getGreen())  / 2, (it.getBlue() + inh.getBlue()) / 2).getRGB();

                }
            }

        }
        return  (itemcolor == Integer.MAX_VALUE) ? ICADColorizer.DEFAULT_SPELL_COLOR : itemcolor;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
        super.addInformation(stack, world, tooltip, mistake);
        if(GuiScreen.isShiftKeyDown()){
            ItemStack inheriting = Companion.getInheriting(stack);
            if(!inheriting.isEmpty()){
                String translatedPrefix = I18n.translateToLocal(com.github.kamefrede.rpsideas.util.Reference.MODID + ".misc.color_inheritance");
                tooltip.add(TextFormatting.GREEN + translatedPrefix + TextFormatting.GRAY + ": " + inheriting.getDisplayName());
            }
            if(mistake.isAdvanced()){
                int color = Companion.getColorFromStack(stack);
                if(color != Integer.MAX_VALUE){
                    String formattedNumber = String.format("%06X", color);
                    if(formattedNumber.length() > 6) formattedNumber = formattedNumber.substring(formattedNumber.length() - 6);
                    String translatedPrefix = I18n.translateToLocal(com.github.kamefrede.rpsideas.util.Reference.MODID + ".misc.color");
                    tooltip.add(TextFormatting.GREEN + translatedPrefix + TextFormatting.GRAY + ": #" + formattedNumber);
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        if(player.isSneaking()) {
            held = new ItemStack(ModItems.drainedColorizer);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }
}
