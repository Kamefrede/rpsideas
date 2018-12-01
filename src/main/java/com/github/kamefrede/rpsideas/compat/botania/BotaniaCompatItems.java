package com.github.kamefrede.rpsideas.compat.botania;

import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.items.components.botania.ItemBlasterAssembly;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.arl.item.ItemMod;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.common.core.handler.PlayerDataHandler;

@Mod.EventBusSubscriber
public class BotaniaCompatItems {
    public static ItemMod blaster;

    public static void botaniaPreInit(){
        blaster = new ItemBlasterAssembly();
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> evt) {
        IForgeRegistry<IRecipe> r = evt.getRegistry();
        if(Loader.isModLoaded("botania")){
            r.register(new RecipeBlasterCADClipp().setRegistryName(new ResourceLocation(Reference.MODID, "blasterclip")));

            r.register(new RecipeBlasterCADLens().setRegistryName(new ResourceLocation(Reference.MODID, "blaster")));
        }

    }
    }

