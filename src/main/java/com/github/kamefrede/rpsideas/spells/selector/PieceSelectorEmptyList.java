package com.github.kamefrede.rpsideas.spells.selector;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.List;

public class PieceSelectorEmptyList extends PieceSelector {
    public PieceSelectorEmptyList(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {

        AxisAlignedBB axis = new AxisAlignedBB(0, 0, 0, 0, 0, 0);


        List<Entity> list = context.caster.getEntityWorld().getEntitiesWithinAABB(Entity.class, axis, (Entity e) -> {
            return e != null  && e != context.caster && e != context.focalPoint && context.isInRadius(e);
        });

        return new EntityListWrapper(list);
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}
