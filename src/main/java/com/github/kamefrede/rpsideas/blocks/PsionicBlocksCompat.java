package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.tiles.TileCADCase;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class PsionicBlocksCompat {

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "cad_case")
    public static final Block cadCase = Blocks.AIR;

    public static void register(IForgeRegistry<Block> reg) {

        reg.register(createBlock(new BlockCADCase(), "cad_case"));
        GameRegistry.registerTileEntity(TileCADCase.class, Reference.MODID + ":case");

    }

    static <B extends Block> B createBlock(B block, String name) {
        ResourceLocation res = new ResourceLocation(Reference.MODID, name);

        block.setRegistryName(res);
        block.setUnlocalizedName(res.getResourceDomain() + "." + res.getResourcePath());

        return block;
    }
}
