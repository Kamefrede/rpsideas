package com.kamefrede.rpsideas.compat.botania;

import com.kamefrede.rpsideas.spells.enabler.botania.IBlasterComponent;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.botania.common.item.ModItems;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;

import javax.annotation.Nonnull;

public class RecipeBlasterCADClip extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        boolean foundGun = false;
        boolean foundClip = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ICAD && ((ICAD) stack.getItem()).getComponentInSlot(stack, EnumCADComponent.ASSEMBLY).getItem() instanceof IBlasterComponent) {
                    if (foundGun)
                        return false;
                    foundGun = true;
                } else if (stack.getItem() == ModItems.clip) {
                    if (foundClip)
                        return false;
                    foundClip = true;
                } else return false; // Found an invalid item, breaking the recipe
            }
        }

        return foundGun && foundClip;
    }


    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack gun = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ICAD)
                gun = stack;
        }

        if (gun.isEmpty()) return gun;

        ItemStack lens = ItemManaGun.getLens(gun);
        ItemManaGun.setLens(gun, ItemStack.EMPTY);
        ItemStack copy = gun.copy();
        ItemManaGun.setClip(copy, true);
        ItemManaGun.setLensAtPos(copy, lens, 0);
        return copy;

    }
}
