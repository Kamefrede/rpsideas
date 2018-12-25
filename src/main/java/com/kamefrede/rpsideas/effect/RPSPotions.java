package com.kamefrede.rpsideas.effect;


import com.kamefrede.rpsideas.effect.base.PotionTypeMod;
import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class RPSPotions {
    public static final Potion psishock = new PotionPsishock();
    public static final Potion psipulse = new PotionPsipulse();

    public static final PotionTypeMod psishockType = new PotionTypeMod(RPSItemNames.PSISHOCK, new PotionEffect(psishock, 160));
    public static final PotionTypeMod psishockLongType = new PotionTypeMod(RPSItemNames.PSISHOCK_LONG, new PotionEffect(psishock, 240));
    public static final PotionTypeMod psishockStrongType = new PotionTypeMod(RPSItemNames.PSISHOCK_STRONG, new PotionEffect(psishock, 125, 1));

    public static final PotionTypeMod psipulseType = new PotionTypeMod(RPSItemNames.PSIPULSE, new PotionEffect(psipulse, 400));
    public static final PotionTypeMod psipulseStrongType = new PotionTypeMod(RPSItemNames.PSIPULSE_STRONG, new PotionEffect(psipulse, 250, 1));
    public static final PotionTypeMod psipulseLongType = new PotionTypeMod(RPSItemNames.PSIPULSE_LONG, new PotionEffect(psipulse, 600));
}
