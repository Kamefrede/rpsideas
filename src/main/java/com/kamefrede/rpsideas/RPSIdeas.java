package com.kamefrede.rpsideas;

import com.kamefrede.rpsideas.command.CommandPsiLearn;
import com.kamefrede.rpsideas.command.CommandPsiUnlearn;
import com.kamefrede.rpsideas.entity.RPSEntities;
import com.kamefrede.rpsideas.entity.botania.EntityPsiManaBurst;
import com.kamefrede.rpsideas.gui.GuiHandler;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.render.ExosuitGlowLayer;
import com.kamefrede.rpsideas.render.LayerAuthorCape;
import com.kamefrede.rpsideas.render.LayerAuthorOccludeElytra;
import com.kamefrede.rpsideas.render.RenderTileCADCase;
import com.kamefrede.rpsideas.spells.base.SpellPieces;
import com.kamefrede.rpsideas.tiles.TileCADCase;
import com.kamefrede.rpsideas.util.RPSDataFixer;
import com.kamefrede.rpsideas.util.RPSKeybindHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;

import java.util.List;
import java.util.Map;


@Mod(modid = RPSIdeas.MODID, name = RPSIdeas.NAME, version = RPSIdeas.VERSION, dependencies = "after:botania;required-after:psi;required-after:ctm;", useMetadata = true)
public class RPSIdeas {

    public static final String MODID = "rpsideas";
    public static final String NAME = "Random PSIdeas";
    public static final String VERSION = "%VERSION%";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.Instance
    public static RPSIdeas INSTANCE;

    public static ModFixs DATA_FIXER;

    // Common

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        SpellPieces.init();
        RPSEntities.init();
        DATA_FIXER = FMLCommonHandler.instance().getDataFixer().init(MODID, RPSDataFixer.parseSemVer(VERSION));

        RPSPacketHandler.initPackets();
        NetworkRegistry.INSTANCE.registerGuiHandler(RPSIdeas.INSTANCE, new GuiHandler());
    }

    // Botania

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

    // Client

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientPreInit(FMLPreInitializationEvent e) {
        RPSKeybindHandler.init();
        RPSEntities.clientInit();
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent e) {
        ClientRegistry.bindTileEntitySpecialRenderer(TileCADCase.class, new RenderTileCADCase());

        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        injectLayers(skinMap.get("default"));
        injectLayers(skinMap.get("slim"));
    }

    @SideOnly(Side.CLIENT)
    private static void injectLayers(RenderPlayer render) {
        if (render != null) {
            render.addLayer(new ExosuitGlowLayer(render));
            render.addLayer(new LayerAuthorCape(render));

            List<LayerRenderer<AbstractClientPlayer>> layers = render.layerRenderers;
            for (int i = 0; i < layers.size(); i++) {
                LayerRenderer layer = layers.get(i);
                if (layer instanceof LayerElytra)
                    layers.set(i, new LayerAuthorOccludeElytra((LayerElytra) layer));
            }

        }
    }

    // Server Commands

    @Mod.EventHandler
    public void serverStart(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandPsiLearn());
        e.registerServerCommand(new CommandPsiUnlearn());
    }

}
