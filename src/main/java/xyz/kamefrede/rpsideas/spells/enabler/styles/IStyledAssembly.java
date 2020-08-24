package xyz.kamefrede.rpsideas.spells.enabler.styles;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICADAssembly;
import vazkii.psi.api.cad.ICADComponent;

/**
 * @author WireSegal
 * Created at 11:33 PM on 2/25/19.
 */
public interface IStyledAssembly extends ICADAssembly, ICADComponent {
    @Override
    default EnumCADComponent getComponentType(ItemStack itemStack) {
        return EnumCADComponent.ASSEMBLY;
    }

    boolean enablesStyle(ItemStack cad, EnumAssemblyStyle style);
}
