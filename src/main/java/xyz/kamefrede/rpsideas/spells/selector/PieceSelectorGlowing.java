package com.kamefrede.rpsideas.spells.selector;

import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.init.MobEffects;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.selector.entity.PieceSelectorNearby;

public class PieceSelectorGlowing extends PieceSelectorNearby {

    public PieceSelectorGlowing(Spell spell) {
        super(spell);
    }

    @Override
    @SuppressWarnings("Guava")
    public Predicate<Entity> getTargetPredicate() {
        return (Entity e) -> e != null && (e instanceof EntityEnderEye || e.isBurning() || e.isGlowing() ||
                (e instanceof EntityLivingBase && ((EntityLivingBase) e).isPotionActive(MobEffects.GLOWING)));
    }
}
