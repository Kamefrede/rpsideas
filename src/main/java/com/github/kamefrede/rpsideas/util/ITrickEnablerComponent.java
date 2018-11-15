package com.github.kamefrede.rpsideas.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;

public interface ITrickEnablerComponent extends ICADComponent {


    enum EnableResult {
        NOT_ENABLED,
        MISSING_REQUIREMENT,
        SUCCESS;

        public static EnableResult fromBoolean(boolean bool) {
            return bool ? SUCCESS : MISSING_REQUIREMENT;
        }
    }
}
