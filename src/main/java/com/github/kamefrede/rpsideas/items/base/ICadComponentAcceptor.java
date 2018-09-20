package com.github.kamefrede.rpsideas.items.base;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;

public interface  ICadComponentAcceptor {
    ItemStack setPiece(ItemStack stack, EnumCADComponent type, ItemStack piece);

    ItemStack getPiece(ItemStack stack, EnumCADComponent type);

    boolean acceptsPiece(ItemStack stack, EnumCADComponent type);
}
