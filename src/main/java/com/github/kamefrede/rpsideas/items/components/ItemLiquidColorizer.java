package com.github.kamefrede.rpsideas.items.components;

import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.items.base.ICadComponentAcceptor;
import com.github.kamefrede.rpsideas.items.base.ItemComponent;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.helpers.ItemNBTHelpers;
import vazkii.psi.api.cad.ICADColorizer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADColorizer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemLiquidColorizer extends ItemComponent implements ICADColorizer, ICadComponentAcceptor {
    @SideOnly(Side.CLIENT)
    @Override
    public int getColor(ItemStack stack) {
        int color = getColorFromStack(stack);

        if(!stack.isEmpty()) {
            ItemStack inheriting = getInheriting(stack);
            if(!inheriting.isEmpty() && inheriting.getItem() instanceof ICADColorizer) {
                int inheritColor = ((ICADColorizer)inheriting.getItem()).getColor(inheriting);

                if(color == Integer.MAX_VALUE) {
                    //The user hasn't dyed this colorizer, so just use the exact color of the inherited item.
                    color = inheritColor;
                } else {
                    //The user did dye their colorizer, so pick a color halfway between.
                    //TODO Yoooo throw a sinewave on this, it will look sick
                    int red = (color & 0xFF0000) >> 16;
                    int green = (color & 0x00FF00) >> 8;
                    int blue = color & 0x0000FF;

                    int inheritRed = (inheritColor & 0xFF0000) >> 16;
                    int inheritGreen = (inheritColor & 0x00FF00) >> 8;
                    int inheritBlue = inheritColor & 0x0000FF;

                    int lerp = 128;

                    int mixedRed = red * (1 - lerp) + inheritRed * lerp;
                    int mixedGreen = green * (1 - lerp) + inheritGreen * lerp;
                    int mixedBlue = blue * (1 - lerp) + inheritBlue * lerp;

                    color = ((mixedRed & 0xFF) << 16) | ((mixedGreen & 0xFF) << 8) | (mixedBlue & 0xFF);
                }
            }
        }

        return color == Integer.MAX_VALUE ? ICADColorizer.DEFAULT_SPELL_COLOR : color;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
        super.addInformation(stack, world, tooltip, mistake);

        if(GuiScreen.isShiftKeyDown()) {
            ItemStack inheriting = getInheriting(stack);
            if(!inheriting.isEmpty()) {
                String translatedPrefix = I18n.translateToLocal(Reference.MODID + ".misc.color_inheritance");
                tooltip.add(TextFormatting.GREEN + translatedPrefix + TextFormatting.GRAY + ": " + inheriting.getDisplayName());
            }

            if(mistake.isAdvanced()) {
                int color = getColorFromStack(stack);
                if(color != Integer.MAX_VALUE) {
                    String formattedNumber = String.format("%06X", color);
                    if(formattedNumber.length() > 6) formattedNumber = formattedNumber.substring(formattedNumber.length() - 6);
                    String translatedPrefix = I18n.translateToLocal(Reference.MODID  + ".misc.color");
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

    private static String TAG_COLOR = "Color";
    private static String TAG_INHERITING = "Inheriting";

    private static int getColorFromStack(ItemStack stack) {
        return ItemNBTHelpers.getIntegerOrDefault(stack, TAG_COLOR, Integer.MAX_VALUE);
    }

    private static ItemStack getInheriting(ItemStack stack) {
        return ItemNBTHelpers.getItemStack(stack, TAG_INHERITING);
    }

    private static ItemStack setInheriting(ItemStack stack, ItemStack inheriting) {
        if(inheriting.isEmpty()) {
            ItemNBTHelpers.removeTag(stack, TAG_INHERITING);
        } else {
            ItemNBTHelpers.setItemStack(stack, TAG_INHERITING, inheriting);
        }

        return stack;
    }

    @Nonnull
    @Override
    public ItemStack setPiece(@Nonnull ItemStack stack, @Nonnull EnumCADComponent type, @Nonnull ItemStack piece) {
        return type == EnumCADComponent.DYE ? setInheriting(stack, piece) : stack;
    }

    @Nonnull
    @Override
    public ItemStack getPiece(@Nonnull ItemStack stack, @Nonnull EnumCADComponent type) {
        return type == EnumCADComponent.DYE ? getInheriting(stack) : ItemStack.EMPTY;
    }

    @Override
    public boolean acceptsPiece(@Nonnull ItemStack stack, @Nonnull EnumCADComponent type) {
        return type == EnumCADComponent.DYE;
    }
}
