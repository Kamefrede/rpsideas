package com.kamefrede.rpsideas.compat.jei.craftingTricks;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.compat.jei.JEICompat;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 1:33 PM on 12/21/18.
 */
public class TrickCraftingCategory implements IRecipeCategory<TrickCraftingRecipeJEI> {
    public static final TrickCraftingCategory INSTANCE = new TrickCraftingCategory();
    private static final IDrawable background = JEICompat.helpers.getGuiHelper().createDrawable(
            new ResourceLocation(RPSIdeas.MODID, "textures/gui/jei/trick.png"), 0, 0, 96, 41);
    private static final int INPUT_SLOT = 0;
    private static final int CAD_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;

    @Nonnull
    @Override
    public String getUid() {
        return RPSIdeas.MODID + ".trick";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format("jei." + RPSIdeas.MODID + ".category.trick");
    }

    @Nonnull
    @Override
    public String getModName() {
        return RPSIdeas.MODID;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull TrickCraftingRecipeJEI recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(INPUT_SLOT, true, 0, 5);
        recipeLayout.getItemStacks().init(CAD_SLOT, true, 21, 23);
        recipeLayout.getItemStacks().init(OUTPUT_SLOT, false, 73, 5);

        recipeLayout.getItemStacks().set(ingredients);
    }
}
