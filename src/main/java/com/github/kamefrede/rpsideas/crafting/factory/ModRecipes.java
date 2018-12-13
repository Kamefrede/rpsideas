package com.github.kamefrede.rpsideas.crafting.factory;

import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import org.apache.commons.lang3.tuple.Pair;
import scala.Array;
import vazkii.arl.recipe.RecipeHandler;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.common.crafting.ModCraftingRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class ModRecipes {

    public static final List<ItemStack> examplesockets = Arrays.asList(new ItemStack(ModItems.creativeSocket), new ItemStack(ModItems.wideBandSocket), new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 0)
    , new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 1), new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 2 ), new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 3),
            new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 4));



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
