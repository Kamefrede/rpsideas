package com.kamefrede.rpsideas.crafting.trick;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

/**
 * @author WireSegal
 * Created at 1:43 PM on 12/21/18.
 */
public class TrickRecipe {
    private String piece;
    private Ingredient input;
    private ItemStack output;
    private ItemStack cad;

    public TrickRecipe(String trick, Ingredient input, ItemStack output, ItemStack CAD) {
        piece = trick;
        this.input = input;
        this.output = output;
        this.cad = CAD;
    }

    public String getPiece() {
        return piece;
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getCAD() {
        return cad;
    }
}
