package com.github.kamefrede.rpsideas.items.components;

import com.github.kamefrede.rpsideas.items.base.ItemComponent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCreativeBattery extends ItemComponent {
    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.BATTERY;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.OVERFLOW, -1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
        super.addInformation(stack, world, tooltip, mistake);
    }
}
