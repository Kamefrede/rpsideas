package com.kamefrede.rpsideas.effect;

import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.PotionMod;

public class PotionAffinity extends PotionMod {
    public PotionAffinity() {
        super(RPSItemNames.AFFINITY, false, 0x1D6D9E);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    public static int getMaxAmp() {
        return 4;
    }
}
