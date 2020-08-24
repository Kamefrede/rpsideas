package xyz.kamefrede.rpsideas.spells.enabler;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.cad.ICADComponent;
import vazkii.psi.api.spell.SpellPiece;

public interface ITrickEnablerComponent extends ICADComponent {

    boolean enables(ItemStack cad, ItemStack component, SpellPiece piece);

    enum EnableResult {
        NOT_ENABLED,
        MISSING_REQUIREMENT,
        SUCCESS;

        public static EnableResult fromBoolean(boolean bool) {
            return bool ? SUCCESS : MISSING_REQUIREMENT;
        }
    }
}
