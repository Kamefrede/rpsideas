package com.github.kamefrede.rpsideas.effect;

import com.github.kamefrede.rpsideas.effect.base.PotionPsiChange;
import com.github.kamefrede.rpsideas.util.libs.LibItems;

public class PotionPsishock extends PotionPsiChange {
    protected PotionPsishock() {
        super(LibItems.PSISHOCK, true, 0xFF4D12);
    }

    @Override
    public int getAmpAmount() {
        return -15;
    }

    @Override
    public int getBaseAmount() {
        return -30;
    }
}
