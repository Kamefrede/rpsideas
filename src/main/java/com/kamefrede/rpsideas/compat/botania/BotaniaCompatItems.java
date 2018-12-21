package com.kamefrede.rpsideas.compat.botania;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.effect.RPSPotions;
import com.kamefrede.rpsideas.effect.botania.RPSBrew;
import com.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
import com.kamefrede.rpsideas.util.RPSConfigHandler;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.arl.item.ItemMod;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class BotaniaCompatItems {
    public static ItemMod blaster;

    // Permissive type so that this doesn't become a problem if botania isn't installed
    private static Object psishockBrew, psipulseBrew;

    @SubscribeEvent(priority = EventPriority.HIGH)
    @Optional.Method(modid = "botania")
    public static void registerItems(RegistryEvent.Register<Item> evt) {
        blaster = new ItemBlasterAssembly();
        psishockBrew = new RPSBrew(RPSItemNames.PSISHOCK, 16000, new PotionEffect(RPSPotions.psishock, 300))
                .setNotBloodPendantInfusable();
        psipulseBrew = new RPSBrew(RPSItemNames.PSIPULSE, 32000, new PotionEffect(RPSPotions.psipulse, 600))
                .setNotIncenseInfusable().setNotBloodPendantInfusable();
    }

    @SubscribeEvent
    @Optional.Method(modid = "botania")
    public static void registerRecipes(RegistryEvent.Register<IRecipe> evt) {
        evt.getRegistry().register(new RecipeBlasterCADClip()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, "blasterclip")));

        evt.getRegistry().register(new RecipeBlasterCADLens()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, "blaster")));

        if (psishockBrew instanceof Brew)
            BotaniaAPI.registerBrewRecipe((Brew) psishockBrew,
                    new ItemStack(Items.NETHER_WART),
                    "dustPsi",
                    new ItemStack(Items.FERMENTED_SPIDER_EYE));
        if (RPSConfigHandler.enablePsipulse && psipulseBrew instanceof Brew)
            BotaniaAPI.registerBrewRecipe((Brew) psipulseBrew,
                    new ItemStack(Items.NETHER_WART),
                    "dustPsi",
                    new ItemStack(Items.GHAST_TEAR));


    }
}

