package com.kamefrede.rpsideas.crafting;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RPSRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        RecipeLiquidColorizer recipeLiquidDye = new RecipeLiquidColorizer();
        recipeLiquidDye.setRegistryName(new ResourceLocation(RPSIdeas.MODID, "liquiddye"));
        event.getRegistry().register(recipeLiquidDye);
    }
}
