package com.kamefrede.rpsideas.spells.operator.string;

import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamSpecific;
import vazkii.psi.api.spell.piece.PieceOperator;

/**
 * @author WireSegal
 * Created at 1:27 PM on 12/16/18.
 */
public class PieceOperatorParseNumber extends PieceOperator {

    private SpellParam str;

    public PieceOperatorParseNumber(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(str = new ParamSpecific(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false) {
            @Override
            protected Class<?> getRequiredType() {
                return String.class;
            }
        });
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        String string = getParamValue(context, str);
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException invalid) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);
        }
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
