package xyz.kamefrede.rpsideas.items.components;

import xyz.kamefrede.rpsideas.RPSIdeas;
import xyz.kamefrede.rpsideas.items.base.ItemComponent;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemOptimizedCADCore extends ItemComponent {


    public ItemOptimizedCADCore() {
        super(RPSItemNames.OPTIMIZED_CAD_CORE);
    }

    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.CORE;
    }

    @Override
    protected void registerStats() {
        addStat(EnumCADStat.PROJECTION, 12);
        addStat(EnumCADStat.COMPLEXITY, 16);
    }
}
