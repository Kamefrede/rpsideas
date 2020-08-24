package xyz.kamefrede.rpsideas.spells.operator.list;

import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.List;

public class PieceOperatorListIntersection extends PieceOperator {

    private SpellParam list1;
    private SpellParam list2;

    public PieceOperatorListIntersection(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(list1 = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST1, SpellParam.BLUE, false, false));
        addParam(list2 = new ParamEntityListWrapper(SpellParams.GENERIC_NAME_LIST2, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityListWrapper l1 = SpellHelpers.ensureNonnullList(this, context, list1);
        EntityListWrapper l2 = SpellHelpers.ensureNonnullList(this, context, list2);

        List<Entity> entities = new ArrayList<>(l1.unwrap());
        entities.retainAll(l2.unwrap());
        return new EntityListWrapper(entities);
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}
