package com.kamefrede.rpsideas.blocks;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import com.kamefrede.rpsideas.tiles.TileConjuredPulsar;
import com.kamefrede.rpsideas.tiles.TileCracklingStar;
import com.kamefrede.rpsideas.tiles.TileEthereal;
import com.kamefrede.rpsideas.util.libs.LibBlockNames;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockMod;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSBlocks {

    public static final BlockModNoItem conjuredEthereal = new BlockConjuredEthereal();
    public static final BlockModNoItem conjuredGravityBlock = new BlockConjuredGravityBlock();
    public static final BlockModNoItem conjuredPulsar = new BlockConjuredPulsar();
    public static final BlockModNoItem conjuredPulsarLight = new BlockPulsarLight();
    public static final BlockModNoItem conjuredStar = new BlockConjuredStar();
    public static final BlockMod cadCase = new BlockCADCase();
    public static final BlockMod brightPlate = new BlockPlate(LibBlockNames.BRIGHT_PLATE);
    public static final BlockMod darkPlate = new BlockPlate(LibBlockNames.DARK_PLATE);

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerStateMappers(ModelRegistryEvent e) {

        // ARL doesn't respect itemless blocks

        conjuredEthereal.registerStateMapper();
        conjuredGravityBlock.registerStateMapper();
        conjuredPulsar.registerStateMapper();
        conjuredPulsarLight.registerStateMapper();
        conjuredStar.registerStateMapper();
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<Block> e) {
        GameRegistry.registerTileEntity(TileEthereal.class,
                new ResourceLocation(RPSIdeas.MODID, LibBlockNames.CONJURED_ETHEREAL_BLOCK));
        GameRegistry.registerTileEntity(TileConjuredPulsar.class,
                new ResourceLocation(RPSIdeas.MODID, LibBlockNames.CONJURED_PULSAR_BLOCK));
        GameRegistry.registerTileEntity(TileCracklingStar.class,
                new ResourceLocation(RPSIdeas.MODID, LibBlockNames.CONJURED_STAR_BLOCK));
        GameRegistry.registerTileEntity(TileCADCase.class,
                new ResourceLocation(RPSIdeas.MODID, LibBlockNames.CAD_CASE));
    }
}
