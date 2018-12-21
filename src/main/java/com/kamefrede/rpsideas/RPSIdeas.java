package com.kamefrede.rpsideas;

import com.kamefrede.rpsideas.command.CommandPsiLearn;
import com.kamefrede.rpsideas.command.CommandPsiUnlearn;
import com.kamefrede.rpsideas.entity.RPSEntities;
import com.kamefrede.rpsideas.entity.botania.EntityPsiManaBurst;
import com.kamefrede.rpsideas.gui.GuiHandler;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.render.RenderTileCADCase;
import com.kamefrede.rpsideas.spells.base.SpellPieces;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;


@Mod(modid = RPSIdeas.MODID, name = RPSIdeas.NAME, version = RPSIdeas.VERSION, dependencies = "after:botania;required-after:psi;required-after:ctm;", useMetadata = true)
public class RPSIdeas {

    public static final String MODID = "rpsideas";
    public static final String NAME = "Random PSIdeas";
    public static final String VERSION = "%VERSION%";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.Instance
    public static RPSIdeas INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SpellPieces.init();
        RPSEntities.init();

        RPSPacketHandler.initPackets();
        NetworkRegistry.INSTANCE.registerGuiHandler(RPSIdeas.INSTANCE, new GuiHandler());
    }

    @Mod.EventHandler
    @Optional.Method(modid = "botania")
    public void preInitBotania(FMLPreInitializationEvent event) {
        RPSEntities.initBotania();
    }

    @Mod.EventHandler
    @Optional.Method(modid = "botania")
    public void initBotania(FMLInitializationEvent event) {
        FMLInterModComms.sendMessage("projecte", "interdictionblacklist", EntityPsiManaBurst.class.getCanonicalName());
    }

    @Mod.EventHandler
    @Optional.Method(modid = "botania")
    public void postInitBotania(FMLPostInitializationEvent event) {
        BotaniaAPI.blacklistEntityFromGravityRod(EntityPsiManaBurst.class);
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientPreInit(FMLPreInitializationEvent e) {
        RPSEntities.clientInit();
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent e) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileCADCase.class, new RenderTileCADCase());
    }

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandPsiLearn());
        e.registerServerCommand(new CommandPsiUnlearn());
    }

}
