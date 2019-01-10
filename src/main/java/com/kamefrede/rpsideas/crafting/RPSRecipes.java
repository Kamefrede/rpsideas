package com.kamefrede.rpsideas.crafting;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.effect.RPSPotions;
import com.kamefrede.rpsideas.util.RPSConfigHandler;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSRecipes {

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeLiquidColorizer()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, RPSItemNames.LIQUID_COLORIZER)));
        event.getRegistry().register(new RecipeAttachBattlecaster()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, RPSItemNames.INTEGRATED_BATTLECASTER)));
        event.getRegistry().register(new RecipeCuffKeyAttach()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, RPSItemNames.PSI_CUFFS)));



        if (RPSConfigHandler.enablePsipulse) {
            addCompletePotionRecipes(CraftingHelper.getIngredient("dustPsi"), PotionTypes.AWKWARD, RPSPotions.psipulseType, RPSPotions.psipulseLongType, RPSPotions.psipulseStrongType);
            addPotionConversionRecipes(CraftingHelper.getIngredient(new ItemStack(Items.FERMENTED_SPIDER_EYE)),
                    RPSPotions.psipulseType, RPSPotions.psipulseLongType, RPSPotions.psipulseStrongType,
                    RPSPotions.psishockType, RPSPotions.psishockLongType, RPSPotions.psishockStrongType);
        } else
            addCompletePotionRecipes(CraftingHelper.getIngredient("dustPsi"), PotionTypes.AWKWARD, RPSPotions.psishockType, RPSPotions.psishockLongType, RPSPotions.psishockStrongType);

    }


    private static void addCompletePotionRecipes(Ingredient predicate, PotionType fromType, PotionType normalType, @Nullable PotionType longType, @Nullable PotionType strongType) {
        if (fromType == PotionTypes.AWKWARD)
            PotionHelper.addMix(PotionTypes.WATER, predicate, PotionTypes.MUNDANE);
        PotionHelper.addMix(fromType, predicate, normalType);
        if (longType != null) PotionHelper.addMix(normalType, Items.REDSTONE, longType);
        if (strongType != null) PotionHelper.addMix(normalType, Items.GLOWSTONE_DUST, strongType);
    }

    private static void addPotionConversionRecipes(Ingredient predicate, PotionType fromTypeNormal, @Nullable PotionType fromTypeLong, @Nullable PotionType fromTypeStrong, PotionType normalType, @Nullable PotionType longType, @Nullable PotionType strongType) {
        addCompletePotionRecipes(predicate, fromTypeNormal, normalType, longType, strongType);
        if (longType != null && fromTypeLong != null)
            PotionHelper.addMix(fromTypeLong, predicate, longType);
        if (strongType != null && fromTypeStrong != null)
            PotionHelper.addMix(fromTypeStrong, predicate, strongType);
    }
}
