package com.kamefrede.rpsideas.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author WireSegal
 * Created at 10:29 PM on 12/20/18.
 */
public class IngredientCADComponent extends Ingredient {

    private final EnumCADComponent component;
    private final Ingredient excluded;

    public static NonNullList<ItemStack> defaults(EnumCADComponent component) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        for (Item item : Item.REGISTRY) {
            if (item instanceof ICADComponent) {
                NonNullList<ItemStack> inTab = NonNullList.create();
                item.getSubItems(CreativeTabs.SEARCH, inTab);
                for (ItemStack stack : inTab) {
                    if (((ICADComponent) item).getComponentType(stack) == component)
                        stacks.add(stack);
                }
            }
        }

        return stacks;
    }

    private static ItemStack[] getForIngredient(EnumCADComponent component, @Nullable Ingredient excluded) {
        NonNullList<ItemStack> valid = IngredientCADComponent.defaults(component);

        if (excluded != null)
            valid.removeIf(excluded);

        return valid.toArray(new ItemStack[0]);
    }

    public IngredientCADComponent(EnumCADComponent component, @Nullable Ingredient excluded) {
        super(getForIngredient(component, excluded));
        this.component = component;
        this.excluded = excluded == null ? Ingredient.EMPTY : excluded;
    }

    @Override
    public boolean apply(@Nullable ItemStack stack) {
        if (stack == null || stack.isEmpty() || excluded.test(stack))
            return false;

        Item item = stack.getItem();
        return item instanceof ICADComponent && ((ICADComponent) item).getComponentType(stack) == component;
    }

    public static class Factory implements IIngredientFactory {
        @Nonnull
        @Override
        public Ingredient parse(JsonContext context, JsonObject json) {
            if (!json.has("component"))
                throw new JsonParseException("No component for a component ingredient");

            String name = json.get("component").getAsString();
            EnumCADComponent type = null;
            for (EnumCADComponent component : EnumCADComponent.values()) {
                if (name.equalsIgnoreCase(component.name())) {
                    type = component;
                    break;
                }
            }

            if (type == null)
                throw new JsonParseException("Type " + name + " isn't a valid component type");


            JsonElement ex = json.get("excluded");
            Ingredient excluded = ex == null || ex.isJsonNull() ? null : CraftingHelper.getIngredient(ex, context);

            return new IngredientCADComponent(type, excluded);
        }
    }
}
