package com.kamefrede.rpsideas.crafting;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

/**
 * @author WireSegal
 * Created at 10:29 PM on 12/20/18.
 */
public class RecipeCADShaped extends ShapedRecipes {
    public RecipeCADShaped(String group, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        super(group, width, height, ingredients, result);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = super.getCraftingResult(inv);
        RecipeCADShapeless.transferComponents(inv, stack);

        return stack;
    }

    public static class Factory implements IRecipeFactory {
        // Copypasta from CraftingHelper
        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            String group = JsonUtils.getString(json, "group", "");
            //if (!group.isEmpty() && group.indexOf(':') == -1)
            //    group = context.getModId() + ":" + group;

            Map<Character, Ingredient> ingMap = Maps.newHashMap();
            for (Map.Entry<String, JsonElement> entry : JsonUtils.getJsonObject(json, "key").entrySet()) {
                if (entry.getKey().length() != 1)
                    throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only).");
                if (" ".equals(entry.getKey()))
                    throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");

                ingMap.put(entry.getKey().toCharArray()[0], CraftingHelper.getIngredient(entry.getValue(), context));
            }
            ingMap.put(' ', Ingredient.EMPTY);

            JsonArray patternJ = JsonUtils.getJsonArray(json, "pattern");

            if (patternJ.size() == 0)
                throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
            if (patternJ.size() > 3)
                throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");

            String[] pattern = new String[patternJ.size()];
            for (int x = 0; x < pattern.length; ++x) {
                String line = JsonUtils.getString(patternJ.get(x), "pattern[" + x + "]");
                if (line.length() > 3)
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                if (x > 0 && pattern[0].length() != line.length())
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                pattern[x] = line;
            }

            NonNullList<Ingredient> input = NonNullList.withSize(pattern[0].length() * pattern.length, Ingredient.EMPTY);
            Set<Character> keys = Sets.newHashSet(ingMap.keySet());
            keys.remove(' ');

            int x = 0;
            for (String line : pattern) {
                for (char chr : line.toCharArray()) {
                    Ingredient ing = ingMap.get(chr);
                    if (ing == null)
                        throw new JsonSyntaxException("Pattern references symbol '" + chr + "' but it's not defined in the key");
                    input.set(x++, ing);
                    keys.remove(chr);
                }
            }

            if (!keys.isEmpty())
                throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + keys);

            ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
            return new RecipeCADShaped(group, pattern[0].length(), pattern.length, input, result);
        }
    }
}
