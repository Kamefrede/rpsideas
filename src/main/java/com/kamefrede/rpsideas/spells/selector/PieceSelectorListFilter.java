package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceSelectorListFilter extends PieceSelector {

    private SpellParam list;
    private SpellParam num;

    public PieceSelectorListFilter(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(list = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST, SpellParam.CYAN, false, false));
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.PURPLE, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote) return null;
        double numVal = this.getParamValue(context, num);
        EntityListWrapper listVal = this.getParamValue(context, list);
        int val = (int) numVal;
        if (listVal == null || listVal.unwrap().isEmpty())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_LIST);

        if (val >= 0 && val < listVal.unwrap().size())
            return listVal.unwrap().get(val);
        else
            throw new SpellRuntimeException(SpellRuntimeExceptions.OUT_OF_BOUNDS);

    }

    @Override
    public Class<?> getEvaluationType() {
        return Entity.class;
    }
}
