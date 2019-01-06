package com.kamefrede.rpsideas.crafting;

import com.kamefrede.rpsideas.items.ItemPsiCuffKey;
import com.kamefrede.rpsideas.items.ItemPsiCuffs;
import com.teamwizardry.librarianlib.features.helpers.ItemNBTHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class RecipeCuffKeyAttach extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    private static final String TAG_KEYNAME = "rpsideas:cuffKeyName";

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        boolean foundCuff = false;
        boolean cuffHasKey = false;
        boolean foundKey = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemPsiCuffs) {
                    if (foundCuff)
                        return false;
                    cuffHasKey = ItemNBTHelper.getString(stack, TAG_KEYNAME, null) != null;
                    foundCuff = true;
                } else if (stack.getItem() instanceof ItemPsiCuffKey) {
                    if (foundKey)
                        return false;
                    foundKey = stack.hasDisplayName();
                } else return false; // Found an invalid item, breaking the recipe
            }
        }

        return foundCuff && (foundKey != cuffHasKey);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack cuffs = ItemStack.EMPTY;
        ItemStack key = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemPsiCuffs)
                cuffs = stack;
            else if (stack.getItem() instanceof ItemPsiCuffKey)
                key = stack;
        }

        if (cuffs.isEmpty()) return cuffs;

        ItemStack output = cuffs.copy();
        ItemNBTHelper.setString(output, TAG_KEYNAME, key.getDisplayName());
        return output;
    }
}
