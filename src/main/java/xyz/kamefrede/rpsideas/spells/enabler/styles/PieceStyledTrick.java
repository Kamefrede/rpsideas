package xyz.kamefrede.rpsideas.spells.enabler.styles;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.List;

public abstract class PieceStyledTrick extends PieceTrick implements IStyledPiece {
    public PieceStyledTrick(Spell spell) {
        super(spell);
    }

    @Override
    public void addToTooltipAfterShift(List<String> tooltip) {
        IStyledPiece.super.addToTooltip(tooltip);
    }
}
