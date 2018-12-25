package com.kamefrede.rpsideas.util.helpers;

import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider;
import kotlin.jvm.functions.Function2;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.Nullable;

public interface IFlowColorAcceptor extends IItemColorProvider {
    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    default Function2<ItemStack, Integer, Integer> getItemColorFunction() {
        return (stack, tintIndex) -> {
            if (tintIndex == 1) {
                return ClientHelpers.getFlowColor(stack);
            } else return -1;
        };
    }
}
