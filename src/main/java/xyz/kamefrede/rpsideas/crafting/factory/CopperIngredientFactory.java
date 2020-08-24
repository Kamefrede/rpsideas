package com.kamefrede.rpsideas.crafting.factory;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 11:42 PM on 12/20/18.
 */
public class CopperIngredientFactory implements IIngredientFactory {
    @Nonnull
    @Override
    public Ingredient parse(JsonContext context, JsonObject json) {
        if (OreDictionary.getOres("ingotCopper").isEmpty())
            return new OreIngredient("dustRedstone");
        else
            return new OreIngredient("ingotCopper");
    }
}
