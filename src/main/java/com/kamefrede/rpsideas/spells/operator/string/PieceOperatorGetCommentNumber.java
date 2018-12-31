package com.kamefrede.rpsideas.spells.operator.string;

import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetCommentNumber extends PieceOperator {

    public PieceOperatorGetCommentNumber(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        if (this.comment.isEmpty())
            throw new SpellCompilationException(SpellCompilationExceptions.NAN_COMMENT, x, y);
        try {
            Double.parseDouble(this.comment);
        } catch (NumberFormatException ex) {
            throw new SpellCompilationException(SpellCompilationExceptions.NAN_COMMENT, x, y);
        }
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (this.comment.isEmpty())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);

        try {
            return Double.parseDouble(this.comment);
        } catch (NumberFormatException ex) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);
        }
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
