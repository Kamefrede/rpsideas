package com.kamefrede.rpsideas;

import com.kamefrede.rpsideas.proxy.CommonProxy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = RPSIdeas.MODID, name = RPSIdeas.NAME, version = RPSIdeas.VERSION, dependencies = "required-after:psi", useMetadata = true)
public class RPSIdeas {

    public static final String MODID = "rpsideas";
    public static final String COMMON_PROXY_CLASS = "com.kamefrede.rpsideas.proxy.CommonProxy";
    public static final String CLIENT_PROXY_CLASS = "com.kamefrede.rpsideas.proxy.ClientProxy";
    public static final String NAME = "Random PSIdeas";
    public static final String VERSION = "1.11e";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    @SidedProxy(clientSide = CLIENT_PROXY_CLASS, serverSide = COMMON_PROXY_CLASS)
    public static CommonProxy proxy;
    @Mod.Instance
    public static RPSIdeas INSTANCE = null;
    public static boolean avaritiaLoaded;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        avaritiaLoaded = Loader.isModLoaded("avaritia");
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }

}
