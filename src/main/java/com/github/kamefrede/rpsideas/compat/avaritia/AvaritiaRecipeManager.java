package com.github.kamefrede.rpsideas.compat.avaritia;

import com.google.gson.JsonObject;
import morph.avaritia.recipe.extreme.ExtremeCraftingManager;
import morph.avaritia.recipe.extreme.ExtremeShapedRecipe;
import morph.avaritia.recipe.extreme.ExtremeShapelessRecipe;
import morph.avaritia.recipe.extreme.IExtremeRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AvaritiaRecipeManager {

    public static final RegistryNamespaced<ResourceLocation, IExtremeRecipe> REGISTRY = new RegistryNamespaced<>();
    private static Map<ResourceLocation, BiFunction<JsonContext, JsonObject, IExtremeRecipe>> recipeFactories = new HashMap<>();

    public static void init(){
        recipeFactories.put(new ResourceLocation("avaritia:shaped"), ExtremeShapedRecipe::fromJson);
        recipeFactories.put(new ResourceLocation("avaritia:shapeless"), ExtremeShapelessRecipe::fromJson);
    }
}
