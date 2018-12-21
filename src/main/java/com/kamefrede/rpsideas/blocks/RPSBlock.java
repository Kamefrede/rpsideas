package com.kamefrede.rpsideas.blocks;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import net.minecraft.block.material.Material;
import vazkii.arl.block.BlockMod;

/**
 * @author WireSegal
 * Created at 4:01 PM on 12/15/18.
 */
public class RPSBlock extends BlockMod {

    public RPSBlock(String name, Material materialIn, String... variants) {
        super(name, materialIn, variants);
        setCreativeTab(RPSCreativeTab.INSTANCE);
    }

    @Override
    public String getModNamespace() {
        return RPSIdeas.MODID;
    }
}
