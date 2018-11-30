package com.github.kamefrede.rpsideas.spells.trick.potion;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.trick.potion.PieceTrickPotionBase;

public class PieceTrickBlindness extends PieceTrickPotionBase {
    public PieceTrickBlindness(Spell spell) {
        super(spell);
    }

    @Override
    public Potion getPotion() {
       return MobEffects.BLINDNESS;
    }

    @Override
    public boolean hasPower() {
        return false;
    }
}
