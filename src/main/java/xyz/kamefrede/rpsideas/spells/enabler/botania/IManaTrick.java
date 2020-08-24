package com.kamefrede.rpsideas.spells.enabler.botania;

import com.kamefrede.rpsideas.spells.enabler.IComponentPiece;
import com.kamefrede.rpsideas.spells.enabler.ITrickEnablerComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.psi.api.spell.SpellContext;

public interface IManaTrick extends IComponentPiece {
    int manaDrain(SpellContext context);

    @Override
    @Optional.Method(modid = "botania")
    default boolean drainResources(ITrickEnablerComponent enabler, ItemStack component, ItemStack cad, SpellContext context, boolean simulate) {
        return ManaItemHandler.requestManaExact(cad, context.caster, manaDrain(context), !simulate);
    }

    @Override
    default String noResources() {
        return "rpsideas.spellerror.nomana";
    }

    default EnumManaTier tier() {
        return EnumManaTier.BASE;
    }

    @Override
    default String[] requiredObjects() {
        return new String[]{ "rpsideas.requirement." + tier() };
    }
}
