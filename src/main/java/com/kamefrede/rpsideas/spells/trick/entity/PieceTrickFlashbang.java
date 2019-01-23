package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.effect.RPSPotions;
import net.minecraft.potion.Potion;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.common.spell.trick.potion.PieceTrickPotionBase;

public class PieceTrickFlashbang extends PieceTrickPotionBase {

    public PieceTrickFlashbang(Spell spell) {
        super(spell);
    }

    @Override
    public Potion getPotion() {
        return RPSPotions.flashbang;
    }

    @Override
    public int getPotency(int power, int time) throws SpellCompilationException {
        return super.getPotency(power, time) * 2;
    }

}
