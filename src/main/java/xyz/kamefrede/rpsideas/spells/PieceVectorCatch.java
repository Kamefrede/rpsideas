package xyz.kamefrede.rpsideas.spells;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceErrorHandler;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 10:12 PM on 3/16/19.
 */
public class PieceVectorCatch extends PieceErrorHandler {
    public PieceVectorCatch(Spell spell) {
        super(spell);
    }

    @Override
    protected String paramName() {
        return SpellParam.GENERIC_NAME_TARGET;
    }

    @Override
    public boolean catchException(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception) {
        return errorPiece.getEvaluationType() == Vector3.class;
    }

    @Nonnull
    @Override
    public Object supplyReplacementValue(SpellPiece errorPiece, SpellContext context, SpellRuntimeException exception) {
        return new Vector3();
    }
}
