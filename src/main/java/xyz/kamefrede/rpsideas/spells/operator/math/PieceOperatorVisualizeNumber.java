package xyz.kamefrede.rpsideas.spells.operator.math;

import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorVisualizeNumber extends PieceOperator {

    private SpellParam num;
    private SpellParam radix;

    public PieceOperatorVisualizeNumber(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, false, false));
        addParam(radix = new ParamNumber(SpellParams.GENERIC_NAME_RADIX, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        // NO-OP
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return Integer.toString((int) SpellHelpers.getNumber(this, context, num, 0),
                (int) SpellHelpers.getNumber(this, context, radix, 10));
    }

    @Override
    public Class<?> getEvaluationType() {
        return String.class;
    }
}
