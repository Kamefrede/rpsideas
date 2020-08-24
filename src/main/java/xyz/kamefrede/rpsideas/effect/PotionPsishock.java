package xyz.kamefrede.rpsideas.effect;

import xyz.kamefrede.rpsideas.effect.base.PotionPsiChange;
import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;

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
