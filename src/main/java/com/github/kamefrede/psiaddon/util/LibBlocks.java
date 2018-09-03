package com.github.kamefrede.psiaddon.util;

import com.github.kamefrede.psiaddon.blocks.ConjuredEtherealBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LibBlocks {

    @GameRegistry.ObjectHolder("psiam:conjuredblock")
    public static ConjuredEtherealBlock conjuredEtherealBlock;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
    }



}
