package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.RPSItems;
import com.kamefrede.rpsideas.items.base.ICADComponentAcceptor;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider;
import kotlin.jvm.functions.Function2;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADColorizer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class ItemLiquidColorizer extends ItemComponent implements ICADColorizer, IItemColorProvider, ICADComponentAcceptor {

    public ItemLiquidColorizer() {
        super(RPSItemNames.LIQUID_COLORIZER);
    }

    public static int getColorFromStack(ItemStack stack) {
        if (!stack.hasTagCompound())
            return -1;
        return ItemNBTHelper.getInt(stack, "color", -1);
    }

    public static ItemStack getInheriting(ItemStack stack) {
        if (!stack.hasTagCompound())
            return ItemStack.EMPTY;
        NBTTagCompound cmp = ItemNBTHelper.getCompound(stack, "inheriting");
        if (cmp == null)
            return ItemStack.EMPTY;
        return new ItemStack(cmp);
    }

    public static void setInheriting(ItemStack stack, ItemStack inheriting) {
        if (inheriting.isEmpty()) {
            if (ItemNBTHelper.detectNBT(stack)) ItemNBTHelper.getNBT(stack).removeTag("inheriting");
        } else {
            NBTTagCompound nbt = new NBTTagCompound();
            inheriting.writeToNBT(nbt);
            ItemNBTHelper.setCompound(stack, "inheriting", nbt);
        }
    }

    @Override
    public void setPiece(ItemStack stack, EnumCADComponent type, ItemStack piece) {
        if (type != EnumCADComponent.DYE)
            return;
        setInheriting(stack, piece);
    }

    @Override
    public ItemStack getPiece(ItemStack stack, EnumCADComponent type) {
        if (type != EnumCADComponent.DYE)
            return ItemStack.EMPTY;
        return getInheriting(stack);
    }

    @Override
    public boolean acceptsPiece(ItemStack stack, EnumCADComponent type) {
        return type == EnumCADComponent.DYE;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Function2<ItemStack, Integer, Integer> getItemColorFunction() {
        return (stack, layer) -> {
            if (layer == 1) {
                return getColor(stack);
            } else return -1;
        };
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColor(ItemStack stack) {
        int itemcolor = getColorFromStack(stack);
        if (!stack.isEmpty()) {
            ItemStack inheriting = getInheriting(stack);
            if (!inheriting.isEmpty() && inheriting.getItem() instanceof ICADColorizer) {
                int inheritColor = ((ICADColorizer) inheriting.getItem()).getColor(inheriting);
                if (itemcolor == -1)
                    itemcolor = inheritColor;
                else {
                    Color it = new Color(itemcolor);
                    Color inh = new Color(inheritColor);
                    itemcolor = new Color((it.getRed() + inh.getRed()) / 2, (it.getGreen() + inh.getGreen()) / 2, (it.getBlue() + inh.getBlue()) / 2).getRGB();

                }
            }

        }
        return (itemcolor == -1) ? ICADColorizer.DEFAULT_SPELL_COLOR : itemcolor;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
        super.addInformation(stack, world, tooltip, mistake);
        if (GuiScreen.isShiftKeyDown()) {
            ItemStack inheriting = getInheriting(stack);
            if (!inheriting.isEmpty()) {
                String translatedPrefix = I18n.format(RPSIdeas.MODID + ".misc.color_inheritance");
                tooltip.add(TextFormatting.GREEN + translatedPrefix + TextFormatting.GRAY + ": " + inheriting.getDisplayName());
            }
            if (mistake.isAdvanced()) {
                int color = getColorFromStack(stack);
                if (color != Integer.MAX_VALUE) {
                    String formattedNumber = String.format("%06X", color);
                    if (formattedNumber.length() > 6)
                        formattedNumber = formattedNumber.substring(formattedNumber.length() - 6);
                    String translatedPrefix = I18n.format(RPSIdeas.MODID + ".misc.color");
                    tooltip.add(TextFormatting.GREEN + translatedPrefix + TextFormatting.GRAY + ": #" + formattedNumber);
                }
            }
        }
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);

        if (player.isSneaking())
            held = new ItemStack(RPSItems.drainedColorizer);

        return new ActionResult<>(EnumActionResult.SUCCESS, held);
    }
}
