package com.kamefrede.rpsideas.blocks;

import net.minecraft.block.material.Material;
import vazkii.arl.block.BlockMod;

/**
 * @author WireSegal
 * Created at 4:01 PM on 12/15/18.
 */
public class BlockRPS extends BlockMod {

    public BlockRPS(String name, Material materialIn, String... variants) {
        super(name, materialIn, variants);
    }

    @Override
    public String getModNamespace() {
        return null;
    }
}
