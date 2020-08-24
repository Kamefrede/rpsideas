package com.kamefrede.rpsideas.effect;

import com.kamefrede.rpsideas.effect.base.PotionPsiChange;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;

public class PotionPsishock extends PotionPsiChange {
    protected PotionPsishock() {
        super(RPSItemNames.PSISHOCK, true, 0xFF4D12);
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
