package xyz.kamefrede.rpsideas.spells.selector;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;

public class PieceSelectorEmptyList extends PieceSelector {

    public PieceSelectorEmptyList(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        // NO-OP
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return new EntityListWrapper(new ArrayList<>());
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}
