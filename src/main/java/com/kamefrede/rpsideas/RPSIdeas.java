package com.kamefrede.rpsideas;

import com.kamefrede.rpsideas.entity.RPSEntities;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.render.RenderTileCADCase;
import com.kamefrede.rpsideas.spells.base.SpellPieces;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = RPSIdeas.MODID, name = RPSIdeas.NAME, version = RPSIdeas.VERSION, dependencies = "required-after:psi", useMetadata = true)
public class RPSIdeas {

    public static final String MODID = "rpsideas";
    public static final String NAME = "Random PSIdeas";
    public static final String VERSION = "1.11e";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.Instance
    public static RPSIdeas INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SpellPieces.init();
        RPSPacketHandler.initPackets();
        RPSEntities.init();
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent e) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileCADCase.class, new RenderTileCADCase());
    }

}
