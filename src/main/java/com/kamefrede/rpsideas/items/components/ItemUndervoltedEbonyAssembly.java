package com.kamefrede.rpsideas.items.components;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.base.ItemComponent;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.IExtraVariantHolder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class ItemUndervoltedEbonyAssembly extends ItemComponent implements IExtraVariantHolder {
    public ItemUndervoltedEbonyAssembly() {
        super(RPSItemNames.EBONY_UNDERVOLTED_ASSEMBLY);
    }

    public static final String[] CAD_MODELS = {
            "ebony_undervolted_cad"
    };

    @Override
    public EnumCADComponent getComponentType(ItemStack stack) {
        return EnumCADComponent.ASSEMBLY;
    }

    @NotNull
    @Override
    public String[] getExtraVariants() {
        return CAD_MODELS;
    }

    @Override
    protected void addStat(EnumCADStat stat, int value) {
        addStat(EnumCADStat.EFFICIENCY,90);
        addStat(EnumCADStat.POTENCY, 280);
        addStat(EnumCADStat.PROJECTION, 1);
    }

    //TODO 0.8x ebony Potency, +1 projection, casting spells spins up your cad, giving a small, flat efficiency upgrade until it expires
}
