package com.kamefrede.rpsideas;

import com.kamefrede.rpsideas.blocks.RPSBlocks;
import com.kamefrede.rpsideas.effect.RPSPotions;
import com.kamefrede.rpsideas.entity.RPSEntities;
import com.kamefrede.rpsideas.entity.botania.EntityPsiManaBurst;
import com.kamefrede.rpsideas.gui.GuiHandler;
import com.kamefrede.rpsideas.items.RPSItems;
import com.kamefrede.rpsideas.render.ExosuitGlowLayer;
import com.kamefrede.rpsideas.render.LayerAuthorCape;
import com.kamefrede.rpsideas.render.LayerAuthorOccludeElytra;
import com.kamefrede.rpsideas.render.RenderPsiCuffs;
import com.kamefrede.rpsideas.spells.base.RPSPieces;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import com.kamefrede.rpsideas.util.RPSDataFixer;
import com.kamefrede.rpsideas.util.RPSKeybindHandler;
import com.kamefrede.rpsideas.util.RPSSoundHandler;
import com.teamwizardry.librarianlib.core.client.GlowingHandler;
import com.teamwizardry.librarianlib.features.base.item.IGlowingItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;
import vazkii.psi.common.item.base.ModItems;

import java.util.List;
import java.util.Map;


@Mod(modid = RPSIdeas.MODID, name = RPSIdeas.NAME, version = RPSIdeas.VERSION, dependencies = "after:botania;required-after:psi@[r1.1-71,);required-after:librarianlib;required-after:forge@[14.23.5.2795,);", useMetadata = true)
public class RPSIdeas {

    public static final String MODID = "rpsideas";
    public static final String NAME = "Random PSIdeas";
    public static final String VERSION = "%VERSION%";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.Instance
    public static RPSIdeas INSTANCE;

    public static ModFixs DATA_FIXER;

    // Common

    @SideOnly(Side.CLIENT)
    private static void injectLayers(RenderPlayer render) {
        if (render != null) {
            render.addLayer(new ExosuitGlowLayer(render));
            render.addLayer(new LayerAuthorCape(render));
            render.addLayer(new RenderPsiCuffs(render));
            //   render.addLayer(new SansUndertaleEyeGlow(render));

            List<LayerRenderer<AbstractClientPlayer>> layers = render.layerRenderers;
            for (int i = 0; i < layers.size(); i++) {
                LayerRenderer layer = layers.get(i);
                if (layer instanceof LayerElytra)
                    layers.set(i, new LayerAuthorOccludeElytra((LayerElytra) layer));
            }

        }
    }

    // Botania

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        DATA_FIXER = FMLCommonHandler.instance().getDataFixer().init(MODID, RPSDataFixer.parseSemVer(VERSION));

        new RPSCreativeTab();
        new RPSItems();
        new RPSBlocks();
        new RPSPotions();
        new RPSSoundHandler();

        RPSPieces.init();
        RPSEntities.init();

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

    // Client

    @Mod.EventHandler
    @Optional.Method(modid = "botania")
    public void postInitBotania(FMLPostInitializationEvent event) {
        BotaniaAPI.blacklistEntityFromGravityRod(EntityPsiManaBurst.class);
    }

    @Mod.EventHandler
    @Optional.Method(modid = "rspcompat")
    public void easterEggPreInit(FMLInitializationEvent event) {
        LOGGER.info("Sure thing Reskillable PSI Compat!");
    }


    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientPreInit(FMLPreInitializationEvent e) {
        RPSKeybindHandler.init();
        RPSEntities.clientInit();
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void clientInit(FMLInitializationEvent e) {
        GlowingHandler.registerCustomGlowHandler(ModItems.cad, (stack, model) -> IGlowingItem.Helper.wrapperBake(model, false, 1, 2), (stack, model) -> true);

        Map<String, RenderPlayer> skinMap = Minecraft.getMinecraft().getRenderManager().getSkinMap();
        injectLayers(skinMap.get("default"));
        injectLayers(skinMap.get("slim"));
    }

    // Server Commands

}
