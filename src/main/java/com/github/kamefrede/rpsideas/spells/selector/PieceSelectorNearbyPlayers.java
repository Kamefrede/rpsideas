package com.github.kamefrede.rpsideas.spells.selector;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearby;


public class PieceSelectorNearbyPlayers extends PieceSelectorNearby {

    public PieceSelectorNearbyPlayers(Spell spell){
        super(spell);
    }

    @Override
    public com.google.common.base.Predicate<Entity> getTargetPredicate() {
        return (Entity e) -> { return e instanceof EntityPlayer; };
    }
}
