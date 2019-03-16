package com.kamefrede.rpsideas.items.base;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.cad.RegenPsiEvent;

/**
 * @author WireSegal
 * Created at 10:32 AM on 12/16/18.
 */
public interface IRegenerationBattery extends ICADComponent {
    int getRegenerationValue(ItemStack stack);

    default void alterRegenerationBehavior(ItemStack stack, RegenPsiEvent event) {
        event.addRegen(getRegenerationValue(stack));
    }

    @Override
    default EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.BATTERY;
    }
}
