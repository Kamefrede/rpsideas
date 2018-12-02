package com.github.kamefrede.rpsideas.spells.selector;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

public class PieceSelectorListFilter extends PieceSelector {

    SpellParam list;
    SpellParam num;

    public PieceSelectorListFilter(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(list = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST, SpellParam.CYAN, false, false));
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.PURPLE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.world.isRemote) return null;
        Double numVal = this.<Double>getParamValue(context, num);
        EntityListWrapper listVal = this.<EntityListWrapper>getParamValue(context, list);
        int val = numVal.intValue();
        if(listVal == null) throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_LIST);
        if(listVal.unwrap().isEmpty()){
            throw new SpellRuntimeException(SpellRuntimeExceptions.NULL_LIST);
        }
        if(val >= 0 && val < listVal.unwrap().size()){
            return listVal.unwrap().get(val);
        } else {
            throw new SpellRuntimeException(SpellRuntimeExceptions.OUT_OF_BOUNDS);
        }

    }

    @Override
    public Class<?> getEvaluationType() {
        return Entity.class;
    }
}
