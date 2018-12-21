package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class PieceSelectorCasterEnergy extends PieceSelector {

    public PieceSelectorCasterEnergy(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster == null) return null;

        PlayerDataHandler.PlayerData data = SpellHelpers.getPlayerData(context.caster);

        if (data == null) return 0.0;
        return 1.0 * data.availablePsi;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
