package com.kamefrede.rpsideas.crafting;

import com.kamefrede.rpsideas.items.components.ItemLiquidColorizer;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;

public class RecipeLiquidColorizer extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    public boolean matches(@Nonnull InventoryCrafting inv, @Nullable World worldIn) {
        boolean colorizerFound = false;
        boolean dyeFound = false;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemLiquidColorizer) {

                    if (colorizerFound)
                        return false;

                    colorizerFound = true;
                } else {
                    if (!DyeUtils.isDye(stack))
                        return false;

                    dyeFound = true;
                }
            }
        }

        return colorizerFound && dyeFound;
    }

    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack itemstack = ItemStack.EMPTY;
        int r = 0;
        int g = 0;
        int b = 0;
        int colors = 0;

        for (int k = 0; k < inv.getSizeInventory(); ++k) {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if (!itemstack1.isEmpty()) {
                if (itemstack1.getItem() instanceof ItemLiquidColorizer) {

                    if (!itemstack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }

                    itemstack = itemstack1.copy();
                    itemstack.setCount(1);

                    if (ItemLiquidColorizer.getColorFromStack(itemstack1) != Integer.MAX_VALUE) {
                        Color color = new Color(ItemLiquidColorizer.getColorFromStack(itemstack1));
                        r += color.getRed();
                        b += color.getBlue();
                        g += color.getGreen();
                        colors++;
                    }
                } else {
                    if (!DyeUtils.isDye(itemstack1)) {
                        return ItemStack.EMPTY;
                    }
                    float[] color = DyeUtils.colorFromStack(itemstack1).orElse(EnumDyeColor.WHITE).getColorComponentValues();

                    r += (int) (color[0] * 255);
                    g += (int) (color[1] * 255);
                    b += (int) (color[2] * 255);
                    colors++;
                }
            }
        }

        if (itemstack.isEmpty() || colors == 0) {
            return ItemStack.EMPTY;
        } else {
            r /= colors;
            g /= colors;
            b /= colors;
            ItemStack ink = itemstack.copy();
            NBTHelper.setInt(ink, "color", new Color(r, g, b).getRGB());
            return ink;
        }
    }

    @Nonnull
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    public boolean isDynamic() {
        return true;
    }

    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }
}
