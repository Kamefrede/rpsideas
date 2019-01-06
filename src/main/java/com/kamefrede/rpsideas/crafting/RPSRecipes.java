package com.kamefrede.rpsideas.crafting;

import com.google.common.collect.Lists;
import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.crafting.trick.TrickRecipe;
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
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibPieceNames;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSRecipes {

    public static final List<TrickRecipe> trickRecipes = Lists.newArrayList();

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().register(new RecipeLiquidColorizer()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, RPSItemNames.LIQUID_COLORIZER)));
        event.getRegistry().register(new RecipeAttachBattlecaster()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, RPSItemNames.INTEGRATED_BATTLECASTER)));
        event.getRegistry().register(new RecipeCuffKeyAttach()
                .setRegistryName(new ResourceLocation(RPSIdeas.MODID, RPSItemNames.PSI_CUFFS)));



        trickRecipe("", Items.REDSTONE, new ItemStack(ModItems.material), new ItemStack(ModItems.cadAssembly));
        trickRecipe(LibPieceNames.TRICK_INFUSION, Items.GOLD_INGOT, new ItemStack(ModItems.material, 1, 1), new ItemStack(ModItems.cadAssembly));
        trickRecipe(LibPieceNames.TRICK_GREATER_INFUSION, Items.DIAMOND, new ItemStack(ModItems.material, 1, 2), new ItemStack(ModItems.cadAssembly, 1, 2));
        trickRecipe(LibPieceNames.TRICK_EBONY_IVORY, new ItemStack(Items.COAL), new ItemStack(ModItems.material, 1, 5), new ItemStack(ModItems.cadAssembly, 1, 2));
        trickRecipe(LibPieceNames.TRICK_EBONY_IVORY, Items.QUARTZ, new ItemStack(ModItems.material, 1, 6), new ItemStack(ModItems.cadAssembly, 1, 2));

        if (RPSConfigHandler.enablePsipulse) {
            addCompletePotionRecipes(CraftingHelper.getIngredient("dustPsi"), PotionTypes.AWKWARD, RPSPotions.psipulseType, RPSPotions.psipulseLongType, RPSPotions.psipulseStrongType);
            addPotionConversionRecipes(CraftingHelper.getIngredient(new ItemStack(Items.FERMENTED_SPIDER_EYE)),
                    RPSPotions.psipulseType, RPSPotions.psipulseLongType, RPSPotions.psipulseStrongType,
                    RPSPotions.psishockType, RPSPotions.psishockLongType, RPSPotions.psishockStrongType);
        } else
            addCompletePotionRecipes(CraftingHelper.getIngredient("dustPsi"), PotionTypes.AWKWARD, RPSPotions.psishockType, RPSPotions.psishockLongType, RPSPotions.psishockStrongType);

    }

    private static void trickRecipe(String trick, Object input, ItemStack output, ItemStack minAssembly) {
        trickRecipes.add(new TrickRecipe(trick, CraftingHelper.getIngredient(input), output, minAssembly));
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
