package xyz.kamefrede.rpsideas.items.base;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.api.cad.ICADComponent;

public interface ICooldownAssembly extends ICADComponent, ICADAssembly {
    double getCooldownFactor(ItemStack stack);

    @Override
    default EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.ASSEMBLY;
    }
}
