package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.tiles.TileCADCase;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class PsionicBlocksCompat {

    @GameRegistry.ObjectHolder(Reference.MODID + ":" + LibBlocks.CAD_CASE)
    public static final Block cadCase = Blocks.AIR;


    public static void register(IForgeRegistry<Block> reg) {

        reg.register(createBlock(new BlockCADCase(), LibBlocks.CAD_CASE));
        GameRegistry.registerTileEntity(TileCADCase.class, Reference.MODID + ":case");

    }

    static <B extends Block> B createBlock(B block, String name) {
        ResourceLocation res = new ResourceLocation(Reference.MODID, name);

        block.setRegistryName(res);
        block.setTranslationKey(res.getNamespace() + "." + res.getPath());


        return block;
    }
}
