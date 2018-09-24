package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.util.BlockProperties;
import com.github.kamefrede.rpsideas.util.ParamBlockProperties;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceOperator;

public abstract class BasePieceOperatorBlockProperties<T> extends PieceOperator {
    public BasePieceOperatorBlockProperties(Spell spell) {
        super(spell);
    }

    private SpellParam properties;

    @Override
    public void initParams() {
        properties = new ParamBlockProperties(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false);
        addParam(properties);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return getData(context, getParamValue(context, properties));
    }

    abstract T getData(SpellContext context, BlockProperties props) throws SpellRuntimeException;

    @Override
    public abstract Class<T> getEvaluationType();
}
