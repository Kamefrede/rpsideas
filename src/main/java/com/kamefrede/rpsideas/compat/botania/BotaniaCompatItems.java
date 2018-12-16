package com.kamefrede.rpsideas.compat.botania;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.arl.item.ItemMod;

@Mod.EventBusSubscriber
public class BotaniaCompatItems {
    public static ItemMod blaster;

    @SubscribeEvent
    @Optional.Method(modid = "botania")
    public static void registerItem(RegistryEvent.Register<Item> evt) {
        evt.getRegistry().register(blaster = new ItemBlasterAssembly());
    }

    @SubscribeEvent
    @Optional.Method(modid = "botania")
    public static void registerRecipes(RegistryEvent.Register<IRecipe> evt) {
        IForgeRegistry<IRecipe> r = evt.getRegistry();

        r.register(new RecipeBlasterCADClip()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, "blasterclip")));

        r.register(new RecipeBlasterCADLens()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, "blaster")));

    }
}

