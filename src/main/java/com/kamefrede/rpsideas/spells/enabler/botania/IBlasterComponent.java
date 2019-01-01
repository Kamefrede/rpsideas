package com.kamefrede.rpsideas.spells.enabler.botania;

import com.kamefrede.rpsideas.spells.enabler.ITrickEnablerComponent;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;

public interface IBlasterComponent extends ICADAssembly, ITrickEnablerComponent {
    @Override
    default EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.ASSEMBLY;
    }
}
