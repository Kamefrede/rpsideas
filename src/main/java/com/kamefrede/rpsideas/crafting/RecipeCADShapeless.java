package com.kamefrede.rpsideas.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.kamefrede.rpsideas.items.base.ICADComponentAcceptor;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADComponent;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 10:29 PM on 12/20/18.
 */
public class RecipeCADShapeless extends ShapelessRecipes {
    public RecipeCADShapeless(String group, ItemStack output, NonNullList<Ingredient> ingredients) {
        super(group, output, ingredients);
    }

    @Nonnull
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = super.getCraftingResult(inv);
        transferComponents(inv, stack);

        return stack;
    }

    public static void transferComponents(InventoryCrafting inv, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ICADComponentAcceptor) {
            ICADComponentAcceptor acceptor = (ICADComponentAcceptor) stack.getItem();
            for (int i = 0; i < inv.getSizeInventory(); i++) {
                ItemStack stackInSlot = inv.getStackInSlot(i);
                if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof ICADComponent) {
                    ICADComponent component = (ICADComponent) stackInSlot.getItem();
                    EnumCADComponent type = component.getComponentType(stackInSlot);
                    if (acceptor.acceptsPiece(stack, type))
                        acceptor.setPiece(stack, type, stackInSlot);
                }
            }
        }
    }

    public static class Factory implements IRecipeFactory {
        @Override
        public IRecipe parse(JsonContext context, JsonObject json) {
            String group = JsonUtils.getString(json, "group", "");

            NonNullList<Ingredient> ings = NonNullList.create();
            for (JsonElement ele : JsonUtils.getJsonArray(json, "ingredients"))
                ings.add(CraftingHelper.getIngredient(ele, context));

            if (ings.isEmpty())
                throw new JsonParseException("No ingredients for shapeless recipe");
            if (ings.size() > 9)
                throw new JsonParseException("Too many ingredients for shapeless recipe");

            ItemStack itemstack = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);
            return new RecipeCADShapeless(group, itemstack, ings);
        }
    }
}
