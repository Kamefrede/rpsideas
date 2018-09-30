package com.github.kamefrede.rpsideas.spells.selector;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearby;

public class PieceSelectorFallingBlocks extends PieceSelectorNearby {

    public PieceSelectorFallingBlocks(Spell spell) {
        super(spell);
    }

    @Override
    public Predicate<Entity> getTargetPredicate() {
        return (Entity e) -> { return e instanceof EntityFallingBlock; };
    }
}
