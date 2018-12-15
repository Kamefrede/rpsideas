package com.kamefrede.rpsideas.compat.botania;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.arl.item.ItemMod;

@Mod.EventBusSubscriber
public class BotaniaCompatItems {// TODO: 12/15/18 look at
    public static ItemMod blaster;

    public static void botaniaPreInit() {
        blaster = new ItemBlasterAssembly();
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> evt) {
        IForgeRegistry<IRecipe> r = evt.getRegistry();
        if (Loader.isModLoaded("botania")) {
            r.register(new RecipeBlasterCADClipp().setRegistryName(new ResourceLocation(RPSIdeas.MODID, "blasterclip")));

            r.register(new RecipeBlasterCADLens().setRegistryName(new ResourceLocation(RPSIdeas.MODID, "blaster")));
        }

    }
}

