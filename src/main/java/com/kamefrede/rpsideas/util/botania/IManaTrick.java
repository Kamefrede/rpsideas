package com.kamefrede.rpsideas.util.botania;

import net.minecraft.client.resources.I18n;
import vazkii.psi.api.spell.SpellContext;

public interface IManaTrick extends IComponentPiece {
    int manaDrain(SpellContext context, int x, int y);

    default EnumManaTier tier() {
        return EnumManaTier.BASE;
    }

    @Override
    default String[] requiredObjects() {
        return new String[]{I18n.format("rpsideas.requirement." + tier().toString())};
    }
}
