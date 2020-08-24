package xyz.kamefrede.rpsideas.spells.selector;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearby;

public class PieceSelectorFallingBlocks extends PieceSelectorNearby {

    public PieceSelectorFallingBlocks(Spell spell) {
        super(spell);
    }

    @Override
    @SuppressWarnings("Guava")
    public Predicate<Entity> getTargetPredicate() {
        return (Entity e) -> e instanceof EntityFallingBlock;
    }
}
