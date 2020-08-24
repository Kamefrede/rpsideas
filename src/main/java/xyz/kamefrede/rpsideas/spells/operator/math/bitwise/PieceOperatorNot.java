package xyz.kamefrede.rpsideas.spells.operator.math.bitwise;

import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

/**
 * @author WireSegal
 * Created at 7:50 PM on 2/17/19.
 */
public class PieceOperatorNot extends PieceOperator {
    private SpellParam num;

    public PieceOperatorNot(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        int num = (int) SpellHelpers.getNumber(this, context, this.num, 0);

        return (double) (~ num);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
