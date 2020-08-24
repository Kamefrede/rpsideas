package com.kamefrede.rpsideas.effect.botania;

import com.google.common.collect.Lists;
import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;

/**
 * @author WireSegal
 * Created at 12:37 AM on 12/21/18.
 */
public class RPSBrew extends Brew {
    public RPSBrew(String key, int color, int cost, PotionEffect... effects) {
        super(key, key, color, cost, effects);
        BotaniaAPI.registerBrew(this);
    }


    public RPSBrew(String key, int cost, PotionEffect... effects) {
        this(key, PotionUtils.getPotionColorFromEffectList(Lists.newArrayList(effects)), cost, effects);
    }

    @Override
    public String getUnlocalizedName() {
        return RPSIdeas.MODID + ".brew." + super.getUnlocalizedName();
    }
}
