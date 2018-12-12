package com.github.kamefrede.rpsideas.effect;

import com.github.kamefrede.rpsideas.effect.base.PotionPsiChange;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import vazkii.psi.api.cad.ICADColorizer;

public class PotionPsipulse extends PotionPsiChange {
    protected PotionPsipulse() {
        super(LibItems.PSIPULSE, false, ICADColorizer.DEFAULT_SPELL_COLOR);
    }

    @Override
    public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier) {
        PotionEffect shockeffect = getEffect(entityLivingBaseIn, ModPotions.psishock);
        if(shockeffect != null){
            PotionEffect thisEffect = getEffect(entityLivingBaseIn, this);
            PotionEffect newEffect = new PotionEffect(ModPotions.psishock, shockeffect.getDuration() + thisEffect.getDuration(), Math.min(shockeffect.getAmplifier() + thisEffect.getAmplifier() + 1, 127));
            shockeffect.combine(newEffect);
            shockeffect.combine(new PotionEffect(this, 0, amplifier + 1));
        } else
            super.performEffect(entityLivingBaseIn, amplifier);
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
