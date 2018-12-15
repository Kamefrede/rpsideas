package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.items.base.ItemComponent;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCreativeCore extends ItemComponent { // TODO: 12/15/18 look at
    @Override
    public EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.CORE;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.COMPLEXITY, -1);
        addStat(EnumCADStat.PROJECTION, -1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag mistake) {
        super.addInformation(stack, world, tooltip, mistake);
    }
}
