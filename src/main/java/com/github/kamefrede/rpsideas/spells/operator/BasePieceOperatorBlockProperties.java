package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.util.BlockProperties;
import com.github.kamefrede.rpsideas.util.ParamBlockProperties;
import net.minecraft.block.state.IBlockProperties;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceOperator;

public abstract class BasePieceOperatorBlockProperties<T> extends PieceOperator {

    SpellParam properties;

    public BasePieceOperatorBlockProperties(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(properties = new ParamBlockProperties(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return getData(context, getParamValue(context, properties));
    }

    abstract T getData(SpellContext context, BlockProperties props) throws SpellRuntimeException;

    @Override
    public abstract Class<T> getEvaluationType();
}
