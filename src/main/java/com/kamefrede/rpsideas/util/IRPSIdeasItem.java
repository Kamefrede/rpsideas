package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import vazkii.arl.interf.IVariantHolder;

public interface IRPSIdeasItem extends IVariantHolder {
    default String getModNamespace() {
        return RPSIdeas.MODID;
    }
}
