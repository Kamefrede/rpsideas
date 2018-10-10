package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.tiles.TileConjuredPulsar;
import com.github.kamefrede.rpsideas.tiles.TileCracklingStar;
import com.github.kamefrede.rpsideas.tiles.TileEthereal;
import com.github.kamefrede.rpsideas.util.libs.LibBlocks;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.arl.block.BlockMod;

public class ModBlocks {

    public static BlockMod conjuredEthereal;
    public static BlockMod conjuredGravityBlock;
    public static BlockMod conjuredPulsar;
    public static BlockMod conjuredPulsarLight;
    public static BlockMod conjuredStar;


    public static void preInit(){
        conjuredEthereal = new ConjuredEtherealBlock();
        conjuredPulsar = new BlockConjuredPulsar();
        conjuredPulsarLight = new BlockPulsarLight();
        conjuredStar = new BlockConjuredStar();
        conjuredGravityBlock = new BlockConjuredGravityBlock();

        initTileEntities();
    }

    private static void initTileEntities(){
        registerTile(TileEthereal.class, LibBlocks.CONJURED_ETHEREAL_BLOCK);
        registerTile(TileConjuredPulsar.class, LibBlocks.CONJURED_PULSAR_BLOCK);
        registerTile(TileCracklingStar.class, LibBlocks.CONJURED_STAR_BLOCK);


    }


    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, Reference.MODID + ":" + key);
    }
}
