package com.kamefrede.rpsideas.blocks;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import com.kamefrede.rpsideas.tiles.TileConjuredPulsar;
import com.kamefrede.rpsideas.tiles.TileCracklingStar;
import com.kamefrede.rpsideas.tiles.TileEthereal;
import com.kamefrede.rpsideas.util.libs.RPSBlockNames;
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
    public static final BlockCADCase cadCase = new BlockCADCase();
    public static final BlockMod brightPlate = new BlockPlate(RPSBlockNames.BRIGHT_PLATE);
    public static final BlockMod darkPlate = new BlockPlate(RPSBlockNames.DARK_PLATE);

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerStateMappers(ModelRegistryEvent e) {

        // ARL doesn't respect itemless blocks

        conjuredEthereal.registerStateMapper();
        conjuredGravityBlock.registerStateMapper();
        conjuredPulsar.registerStateMapper();
        conjuredPulsarLight.registerStateMapper();
        conjuredStar.registerStateMapper();

        // Nor does it respect blocks with mesh definitions

        cadCase.registerStateMapper();
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<Block> e) {
        GameRegistry.registerTileEntity(TileEthereal.class,
                new ResourceLocation(RPSIdeas.MODID, RPSBlockNames.CONJURED_ETHEREAL_BLOCK));
        GameRegistry.registerTileEntity(TileConjuredPulsar.class,
                new ResourceLocation(RPSIdeas.MODID, RPSBlockNames.CONJURED_PULSAR_BLOCK));
        GameRegistry.registerTileEntity(TileCracklingStar.class,
                new ResourceLocation(RPSIdeas.MODID, RPSBlockNames.CONJURED_STAR_BLOCK));
        GameRegistry.registerTileEntity(TileCADCase.class,
                new ResourceLocation(RPSIdeas.MODID, RPSBlockNames.CAD_CASE));
    }
}
