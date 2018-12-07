package com.github.kamefrede.rpsideas.spells.selector;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearby;

public class PieceSelectorGlowing extends PieceSelectorNearby {
    public PieceSelectorGlowing(Spell spell) {
        super(spell);
    }

    @Override
    public Predicate<Entity> getTargetPredicate() {
        return (Entity e) -> e instanceof EntityLiving && e.isGlowing();
    }
}
