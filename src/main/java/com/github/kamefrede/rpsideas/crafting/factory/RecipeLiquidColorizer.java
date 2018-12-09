package com.github.kamefrede.rpsideas.crafting.factory;

import com.github.kamefrede.rpsideas.items.components.ItemLiquidColorizer;
import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.DyeUtils;
import vazkii.arl.util.ItemNBTHelper;

import java.awt.*;
import java.util.List;

public class RecipeLiquidColorizer extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        List<ItemStack> list = Lists.<ItemStack>newArrayList();


        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack itemstack1 = inv.getStackInSlot(i);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemLiquidColorizer)
                {

                    if (!itemstack.isEmpty())
                    {
                        return false;
                    }

                    itemstack = itemstack1;
                }
                else
                {
                    if (!DyeUtils.isDye(itemstack1))
                    {
                        return false;
                    }

                    list.add(itemstack1);
                }
            }
        }

        return !itemstack.isEmpty() && !list.isEmpty();
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        float r = 0;
        float g = 0;
        float b = 0;
        float colors = 0;
        ItemLiquidColorizer itemcolorizer;

        for (int k = 0; k < inv.getSizeInventory(); ++k)
        {
            ItemStack itemstack1 = inv.getStackInSlot(k);

            if (!itemstack1.isEmpty())
            {
                if (itemstack1.getItem() instanceof ItemLiquidColorizer)
                {
                    itemcolorizer = (ItemLiquidColorizer) itemstack1.getItem();

                    if (!itemstack.isEmpty())
                    {
                        return ItemStack.EMPTY;
                    }

                    itemstack = itemstack1.copy();
                    itemstack.setCount(1);

                    if (itemcolorizer.getColor(itemstack1) != Integer.MAX_VALUE)
                    {
                        Color color = new Color(itemcolorizer.getColor(itemstack1));
                        r += color.getRed();
                        b += color.getBlue();
                        g += color.getGreen();
                        colors++;
                    }
                }
                else
                {
                    if (!DyeUtils.isDye(itemstack1))
                    {
                        return ItemStack.EMPTY;
                    }
                    Color color  = new Color(DyeUtils.colorFromStack(itemstack1).get().getColorValue());
                    r += color.getRed();
                    g += color.getGreen();
                    b += color.getBlue();
                    colors++;
                }
            }
        }

        if (itemstack.isEmpty() || colors == 0)
        {
            return ItemStack.EMPTY;
        }
        else
        {
            r /= colors;
            g /= colors;
            b /= colors;
            ItemStack ink = itemstack.copy();
            ItemNBTHelper.setInt(ink, "color", new Color(r,g,b).getRGB());
            return ink;
        }
    }

    /**
     * Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one
     * possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack.
     */
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }


    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonnulllist.size(); ++i)
        {
            ItemStack itemstack = inv.getStackInSlot(i);
            nonnulllist.set(i, ForgeHooks.getContainerItem(itemstack));
        }

        return nonnulllist;
    }

    /**
     * If true, this recipe does not appear in the recipe book and does not respect recipe unlocking (and the
     * doLimitedCrafting gamerule)
     */
    public boolean isDynamic()
    {
        return true;
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }
}
