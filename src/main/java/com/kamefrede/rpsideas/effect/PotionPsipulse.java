package com.kamefrede.rpsideas.effect;

import com.kamefrede.rpsideas.effect.base.PotionPsiChange;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import vazkii.psi.api.cad.ICADColorizer;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PotionPsipulse extends PotionPsiChange {
    protected PotionPsipulse() {
        super(RPSItemNames.PSIPULSE, false, ICADColorizer.DEFAULT_SPELL_COLOR);
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
        if (PotionMod.Companion.hasEffect(entity, RPSPotions.psishock)) {
            PotionEffect shockEffect = Objects.requireNonNull(PotionMod.Companion.getEffect(entity, RPSPotions.psishock));
            PotionEffect thisEffect = Objects.requireNonNull(getEffect(entity));
            PotionEffect newEffect = new PotionEffect(RPSPotions.psishock,
                    shockEffect.getDuration() + thisEffect.getDuration(),
                    Math.min(shockEffect.getAmplifier() + thisEffect.getAmplifier() + 1, 127));
            shockEffect.combine(newEffect);
            entity.removeActivePotionEffect(RPSPotions.psipulse);
        } else
            super.performEffect(entity, amplifier);
    }

    @Override
    public int getAmpAmount() {
        return 10;
    }

    @Override
    public int getBaseAmount() {
        return 20;
    }
}
