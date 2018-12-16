package com.kamefrede.rpsideas.crafting.factory;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.RPSItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.common.item.base.ModItems;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber
public class RPSRecipes {

    public static final List<ItemStack> examplesockets = Arrays.asList(
            new ItemStack(RPSItems.creativeSocket),
            new ItemStack(RPSItems.wideBandSocket),
            new ItemStack(ModItems.cadSocket, 1, 0),
            new ItemStack(ModItems.cadSocket, 1, 1),
            new ItemStack(ModItems.cadSocket, 1, 2),
            new ItemStack(ModItems.cadSocket, 1, 3),
            new ItemStack(ModItems.cadSocket, 1, 4));

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeLiquidColorizer recipeLiquidDye = new RecipeLiquidColorizer();
        recipeLiquidDye.setRegistryName(new ResourceLocation(RPSIdeas.MODID, "liquiddye"));
        event.getRegistry().register(recipeLiquidDye);
    }
}
