package com.github.kamefrede.rpsideas.util;

import vazkii.arl.interf.IVariantHolder;

public interface IRPSIdeasItem extends IVariantHolder {
    default String getModNamespace() {
        return Reference.MODID;
    }
}
