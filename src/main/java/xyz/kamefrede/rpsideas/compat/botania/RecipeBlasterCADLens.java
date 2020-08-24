package xyz.kamefrede.rpsideas.compat.botania;

import xyz.kamefrede.rpsideas.spells.enabler.botania.IBlasterComponent;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.mana.ILensControl;
import vazkii.botania.common.item.ItemManaGun;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;

import javax.annotation.Nonnull;

public class RecipeBlasterCADLens extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        boolean foundLens = false;
        boolean foundCAD = false;
        ItemStack cad = ItemStack.EMPTY;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ICAD && ((ICAD) stack.getItem()).getComponentInSlot(stack, EnumCADComponent.ASSEMBLY).getItem() instanceof IBlasterComponent) {
                    if (foundCAD)
                        return false;
                    foundCAD = true;
                    cad = stack;
                } else if (stack.getItem() instanceof ILens) {
                    if (foundLens)
                        return false;

                    if (!(stack.getItem() instanceof ILensControl) || !((ILensControl) stack.getItem()).isControlLens(stack))
                        foundLens = true;
                    else return false;
                } else return false; // Found an invalid item, breaking the recipe
            }
        }

        return foundCAD && ((!ItemManaGun.getLens(cad).isEmpty() ^ foundLens));

    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack lens = ItemStack.EMPTY;
        ItemStack gun = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ICAD)
                    gun = stack;
                else if (stack.getItem() instanceof ILens)
                    lens = stack;
            }
        }
        if (gun.isEmpty())
            return ItemStack.EMPTY;

        ItemStack copy = gun.copy();
        ItemManaGun.setLens(copy, lens);

        return copy;
    }

    @Nonnull
    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> ret = NonNullList.withSize(9, ItemStack.EMPTY);
        int cadIndex = -1;
        ItemStack cad = ItemStack.EMPTY;

        for (int i = 0; i < ret.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ICAD) {
                cad = stack;
                cadIndex = i;
            } else
                ret.set(i, ForgeHooks.getContainerItem(stack));
        }

        if (!cad.isEmpty() && cadIndex != -1 && !ItemManaGun.getLens(cad).isEmpty())
            ret.set(cadIndex, ItemManaGun.getLens(cad));
        return ret;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }


}
