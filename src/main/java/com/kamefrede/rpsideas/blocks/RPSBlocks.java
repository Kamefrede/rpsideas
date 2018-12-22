package com.kamefrede.rpsideas.blocks;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import com.kamefrede.rpsideas.tiles.TileConjuredPulsar;
import com.kamefrede.rpsideas.tiles.TileCracklingStar;
import com.kamefrede.rpsideas.tiles.TileEthereal;
import com.kamefrede.rpsideas.util.libs.RPSBlockNames;
import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
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
    public static final BlockCADCase[] cadCases = new BlockCADCase[16];
    public static final BlockMod brightPlate = new BlockPlate(RPSBlockNames.BRIGHT_PLATE);
    public static final BlockMod darkPlate = new BlockPlate(RPSBlockNames.DARK_PLATE);

    static {
        for (int i = 0; i < cadCases.length; i++) {
            EnumDyeColor dye = EnumDyeColor.byMetadata(i);
            cadCases[i] = new BlockCADCase(RPSBlockNames.CAD_CASE + "_" + dye.getName(), dye);
        }
    }

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
                new ResourceLocation(RPSIdeas.MODID, RPSBlockNames.CONJURED_ETHEREAL_BLOCK));
        GameRegistry.registerTileEntity(TileConjuredPulsar.class,
                new ResourceLocation(RPSIdeas.MODID, RPSBlockNames.CONJURED_PULSAR_BLOCK));
        GameRegistry.registerTileEntity(TileCracklingStar.class,
                new ResourceLocation(RPSIdeas.MODID, RPSBlockNames.CONJURED_STAR_BLOCK));
        GameRegistry.registerTileEntity(TileCADCase.class,
                new ResourceLocation(RPSIdeas.MODID, RPSBlockNames.CAD_CASE_TILE));
    }
}
