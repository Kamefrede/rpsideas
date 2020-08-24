package xyz.kamefrede.rpsideas.spells.enabler;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.List;

public abstract class PieceComponentTrick extends PieceTrick implements IComponentPiece {
    public PieceComponentTrick(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return IComponentPiece.super.execute(context);
    }

    @Override
    public void addToTooltipAfterShift(List<String> tooltip) {
        IComponentPiece.super.addToTooltip(tooltip);
    }
}
