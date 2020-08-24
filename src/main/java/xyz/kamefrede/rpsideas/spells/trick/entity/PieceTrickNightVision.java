package xyz.kamefrede.rpsideas.spells.trick.entity;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.trick.potion.PieceTrickPotionBase;

public class PieceTrickNightVision extends PieceTrickPotionBase {
    public PieceTrickNightVision(Spell spell) {
        super(spell);
    }

    @Override
    public Potion getPotion() {
        return MobEffects.NIGHT_VISION;
    }

    @Override
    public boolean hasPower() {
        return false;
    }
}
