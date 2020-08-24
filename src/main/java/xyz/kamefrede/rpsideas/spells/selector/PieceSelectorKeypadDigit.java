package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class PieceSelectorKeypadDigit extends PieceSelector {

    public static final String TAG_KEYPAD_DIGIT = "rpsideas:keypad";

    public PieceSelectorKeypadDigit(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {

        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);

        if (data.getCustomData().hasKey(TAG_KEYPAD_DIGIT))
            return data.getCustomData().getInteger(TAG_KEYPAD_DIGIT) * 1.0;

        throw new SpellRuntimeException(SpellRuntimeExceptions.NO_DIGIT);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
