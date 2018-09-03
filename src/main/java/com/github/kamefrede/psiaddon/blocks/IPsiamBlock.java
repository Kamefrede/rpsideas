package com.github.kamefrede.psiaddon.blocks;

import com.github.kamefrede.psiaddon.util.Reference;
import vazkii.arl.interf.IModBlock;

public interface IPsiamBlock extends IModBlock {

    @Override
    default String getModNamespace(){
        return Reference.MODID;
    }
}
