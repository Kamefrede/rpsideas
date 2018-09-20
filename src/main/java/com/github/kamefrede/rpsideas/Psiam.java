package com.github.kamefrede.rpsideas;

import com.github.kamefrede.rpsideas.proxy.CommonProxy;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:psi", useMetadata = true)
public class Psiam {

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.Instance
    public static Psiam INSTANCE = null;

    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        proxy.preInit(event);

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
        proxy.postInit(e);
    }

}
