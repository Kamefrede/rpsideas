package xyz.kamefrede.rpsideas.crafting;

import xyz.kamefrede.rpsideas.items.RPSItems;
import xyz.kamefrede.rpsideas.items.components.ItemIntegratedBattlecaster;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

import static xyz.kamefrede.rpsideas.items.components.ItemIntegratedBattlecaster.*;

public class RecipeAttachBattlecaster extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

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
        boolean foundTool = false;
        boolean toolHasBattlecaster = false;
        boolean foundBattlecaster = false;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (canHaveBattlecaster(stack)) {
                    if (foundTool)
                        return false;
                    toolHasBattlecaster = hasBattlecaster(stack);
                    foundTool = true;
                } else if (stack.getItem() instanceof ItemIntegratedBattlecaster) {
                    if (foundBattlecaster)
                        return false;
                    foundBattlecaster = true;
                } else return false; // Found an invalid item, breaking the recipe
            }
        }

        return foundTool && (foundBattlecaster != toolHasBattlecaster);
    }

    @Nonnull
    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            ItemStack inSlot = inv.getStackInSlot(i);
            if (hasBattlecaster(inSlot))
                remaining.set(i, new ItemStack(RPSItems.battlecaster));
            else
                remaining.set(i, ForgeHooks.getContainerItem(inSlot));

        }
        return remaining;
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        ItemStack tool = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (canHaveBattlecaster(stack))
                tool = stack;
        }

        if (tool.isEmpty()) return tool;

        ItemStack output = tool.copy();
        setHasBattlecaster(output, !hasBattlecaster(output));
        return output;
    }

}
