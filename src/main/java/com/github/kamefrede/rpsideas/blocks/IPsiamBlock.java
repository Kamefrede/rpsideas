package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.util.Reference;
import vazkii.arl.interf.IModBlock;

public interface IPsiamBlock extends IModBlock {

    @Override
    default String getModNamespace(){
        return Reference.MODID;
    }
}
