package com.kamefrede.rpsideas.util.helpers;

import com.teamwizardry.librarianlib.features.base.item.IGlowingItem;
import com.teamwizardry.librarianlib.features.base.item.IItemColorProvider;
import kotlin.jvm.functions.Function2;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IFlowColorAcceptor extends IItemColorProvider, IGlowingItem {
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

    @Override
    @SideOnly(Side.CLIENT)
    default int packedGlowCoords(@NotNull ItemStack itemStack, @NotNull IBakedModel model) {
        return 0xf000f0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    default boolean shouldDisableLightingForGlow(@NotNull ItemStack itemStack, @NotNull IBakedModel model) {
        return true;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    default IBakedModel transformToGlow(@NotNull ItemStack itemStack, @NotNull IBakedModel model) {
        return IGlowingItem.Helper.wrapperBake(model, false, 1);
    }
}
