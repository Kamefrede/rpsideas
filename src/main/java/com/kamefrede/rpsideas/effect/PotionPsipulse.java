package com.kamefrede.rpsideas.effect;

import com.kamefrede.rpsideas.effect.base.PotionPsiChange;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import vazkii.psi.api.cad.ICADColorizer;

import javax.annotation.Nonnull;

public class PotionPsipulse extends PotionPsiChange {
    protected PotionPsipulse() {
        super(RPSItemNames.PSIPULSE, false, ICADColorizer.DEFAULT_SPELL_COLOR);
        setRegistryName(RPSItemNames.PSIPULSE);
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
        PotionEffect shockEffect = getEffect(entity, RPSPotions.psishock);
        if (shockEffect != null) {
            PotionEffect thisEffect = getEffect(entity, this);
            PotionEffect newEffect = new PotionEffect(RPSPotions.psishock,
                    shockEffect.getDuration() + thisEffect.getDuration(),
                    Math.min(shockEffect.getAmplifier() + thisEffect.getAmplifier() + 1, 127));
            shockEffect.combine(newEffect);
            shockEffect.combine(new PotionEffect(this, 0, amplifier + 1));
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
