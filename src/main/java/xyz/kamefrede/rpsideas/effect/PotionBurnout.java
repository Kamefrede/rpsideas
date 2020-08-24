package xyz.kamefrede.rpsideas.effect;

import xyz.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.PotionMod;

public class PotionBurnout extends PotionMod {
    public PotionBurnout() {
        super(RPSItemNames.BURNOUT, true, 0xE31E24);
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
