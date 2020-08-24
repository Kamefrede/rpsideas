package com.kamefrede.rpsideas.effect;

import com.kamefrede.rpsideas.util.libs.RPSItemNames;
import com.teamwizardry.librarianlib.features.base.PotionMod;
import net.minecraft.entity.SharedMonsterAttributes;

public class PotionDisableFollowRange extends PotionMod {
    public PotionDisableFollowRange() {
        super(RPSItemNames.DISABLE_FOLLOW_RANGE, false, 0x91acd8);
        registerPotionAttributeModifier(SharedMonsterAttributes.FOLLOW_RANGE, "c4b5c67c-1f29-11e9-ab14-d663bd873d93", -0.9, 1);
    }
}
