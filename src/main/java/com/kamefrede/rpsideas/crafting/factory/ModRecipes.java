package com.kamefrede.rpsideas.crafting.factory;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.arl.recipe.RecipeHandler;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class ModRecipes {// TODO: 12/15/18 look at

    public static final List<ItemStack> examplesockets = Arrays.asList(new ItemStack(ModItems.creativeSocket), new ItemStack(ModItems.wideBandSocket), new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 0)
            , new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 1), new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 2), new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 3),
            new ItemStack(vazkii.psi.common.item.base.ModItems.cadSocket, 1, 4));


    public static void init() {


    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeLiquidColorizer recipeLiquidDye = new RecipeLiquidColorizer();
        recipeLiquidDye.setRegistryName(RPSIdeas.MODID + ":liquiddye");
        event.getRegistry().register(recipeLiquidDye);
    }

    private static void addOreDictRecipe(ItemStack output, Object... recipe) {
        RecipeHandler.addOreDictRecipe(output, recipe);
    }

    private static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
        RecipeHandler.addShapelessOreDictRecipe(output, recipe);
    }
}
