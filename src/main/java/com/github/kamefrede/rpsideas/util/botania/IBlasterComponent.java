package com.github.kamefrede.rpsideas.util.botania;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;

public interface IBlasterComponent extends ICADAssembly, ITrickEnablerComponent {
    @Override
    default EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.ASSEMBLY;
    }
}
