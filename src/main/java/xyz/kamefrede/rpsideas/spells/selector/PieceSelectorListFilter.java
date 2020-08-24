package xyz.kamefrede.rpsideas.spells.selector;

import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
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
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.PURPLE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double numVal = SpellHelpers.getNumber(this, context, num, 0);
        EntityListWrapper listVal = SpellHelpers.ensureNonnullOrEmptyList(this, context, list);
        int val = (int) numVal;

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
