package com.kamefrede.rpsideas.proxy;

import com.kamefrede.rpsideas.crafting.factory.ModRecipes;
import com.kamefrede.rpsideas.entity.ModEntities;
import com.kamefrede.rpsideas.items.ModItems;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.spells.base.SpellPieces;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {// TODO: 12/15/18 look at

    //Config instance
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e) {
        ConfigHandler.initConfig(e.getSuggestedConfigurationFile());
        SpellPieces.init();
        RPSPacketHandler.initPackets();
        ModEntities.init();
        ModItems.preInit();
        ModRecipes.init();
    }

    public void init(FMLInitializationEvent e) {

    }

    public void postInit(FMLPostInitializationEvent e) {
    }

}
