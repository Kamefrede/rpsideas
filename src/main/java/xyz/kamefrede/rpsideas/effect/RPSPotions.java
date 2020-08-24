package com.kamefrede.rpsideas.effect;


import com.kamefrede.rpsideas.effect.base.PotionTypeMod;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.potion.PotionEffect;

public class RPSPotions {
    public static final PotionMod psishock = new PotionPsishock();
    public static final PotionMod psipulse = new PotionPsipulse();
    public static final PotionMod unseeing = new PotionDisableFollowRange();
    public static final PotionFlashbang flash = new PotionFlashbang();
    public static final PotionMod burnout = new PotionBurnout();
    public static final PotionMod affinity = new PotionAffinity();


    public static final PotionTypeMod psishockType = new PotionTypeMod(RPSItemNames.PSISHOCK, new PotionEffect(psishock, 160));
    public static final PotionTypeMod psishockLongType = new PotionTypeMod(RPSItemNames.PSISHOCK_LONG, new PotionEffect(psishock, 240));
    public static final PotionTypeMod psishockStrongType = new PotionTypeMod(RPSItemNames.PSISHOCK_STRONG, new PotionEffect(psishock, 125, 1));

    public static final PotionTypeMod psipulseType = new PotionTypeMod(RPSItemNames.PSIPULSE, new PotionEffect(psipulse, 400));
    public static final PotionTypeMod psipulseStrongType = new PotionTypeMod(RPSItemNames.PSIPULSE_STRONG, new PotionEffect(psipulse, 250, 1));
    public static final PotionTypeMod psipulseLongType = new PotionTypeMod(RPSItemNames.PSIPULSE_LONG, new PotionEffect(psipulse, 600));
}
