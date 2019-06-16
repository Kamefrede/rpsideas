package com.kamefrede.rpsideas.effect;

import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PotionAffinity extends PotionMod {
    public PotionAffinity() {
        super(RPSItemNames.AFFINITY, false, 0x1D6D9E);
    }

    @Override
    public void performEffect(@NotNull EntityLivingBase entity, int amplifier) {
        if (PotionMod.Companion.hasEffect(entity, RPSPotions.burnout)) {
            PotionEffect shockEffect = Objects.requireNonNull(PotionMod.Companion.getEffect(entity, RPSPotions.burnout));
            PotionEffect thisEffect = Objects.requireNonNull(getEffect(entity));
            PotionEffect newEffect = new PotionEffect(RPSPotions.burnout,
                    shockEffect.getDuration() + thisEffect.getDuration(),
                    Math.min(shockEffect.getAmplifier() + thisEffect.getAmplifier() + 1, PotionBurnout.getMaxAmp()));
            shockEffect.combine(newEffect);
            entity.removeActivePotionEffect(RPSPotions.affinity);
        } else
            super.performEffect(entity, amplifier);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    public static int getMaxAmp() {
        return 3;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
