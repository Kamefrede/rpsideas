package com.github.kamefrede.rpsideas.crafting.factory;

import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.common.crafting.ModCraftingRecipes;

@Mod.EventBusSubscriber
public class ModRecipes {


    public static void init(){


    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeLiquidColorizer recipeLiquidDye = new RecipeLiquidColorizer();
        recipeLiquidDye.setRegistryName(Reference.MODID + ":liquiddye");
        event.getRegistry().register(recipeLiquidDye);
    }

    private static void addOreDictRecipe(ItemStack output, Object... recipe) {
        RecipeHandler.addOreDictRecipe(output, recipe);
    }

    private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
        RecipeHandler.addShapelessOreDictRecipe(output, recipe);
    }
}
