package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.tiles.TileEthereal;
import com.github.kamefrede.rpsideas.util.LibBlocks;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.arl.block.BlockMod;

public class ModBlocks {

    public static BlockMod conjuredEthereal;

    public static void preInit(){
        conjuredEthereal = new ConjuredEtherealBlock();

        initTileEntities();
    }

    private static void initTileEntities(){
        registerTile(TileEthereal.class, LibBlocks.CONJURED_ETHEREAL_BLOCK);

    }

    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, Reference.MODID + ":" + key);
    }
}
