package xyz.kamefrede.rpsideas.items.base;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import vazkii.psi.api.cad.EnumCADComponent;

public interface ICADComponentAcceptor {
    void setPiece(ItemStack stack, EnumCADComponent type, NonNullList<ItemStack> piece);

    NonNullList<ItemStack> getPiece(ItemStack stack, EnumCADComponent type);

    boolean acceptsPiece(ItemStack stack, EnumCADComponent type);
}
