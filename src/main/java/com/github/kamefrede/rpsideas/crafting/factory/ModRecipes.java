package com.github.kamefrede.rpsideas.crafting.factory;

import com.github.kamefrede.rpsideas.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.common.crafting.ModCraftingRecipes;

public class ModRecipes {

    public static void init(){
    }

    private static void addOreDictRecipe(ItemStack output, Object... recipe) {
        RecipeHandler.addOreDictRecipe(output, recipe);
    }

    private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
        RecipeHandler.addShapelessOreDictRecipe(output, recipe);
    }
}
