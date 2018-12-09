package com.github.kamefrede.rpsideas.proxy;

import com.github.kamefrede.rpsideas.blocks.ModBlocks;
import com.github.kamefrede.rpsideas.crafting.factory.ModRecipes;
import com.github.kamefrede.rpsideas.entity.ModEntities;
import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.network.RPSPacketHandler;
import com.github.kamefrede.rpsideas.spells.base.SpellPieces;
import com.github.kamefrede.rpsideas.util.RPSEventHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

    //Config instance
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e){
        ConfigHandler.initConfig(e.getSuggestedConfigurationFile());
        SpellPieces.init();
        RPSPacketHandler.initPackets();
        ModEntities.init();
        ModItems.preInit();
        MinecraftForge.EVENT_BUS.register(new RPSEventHandler.Handler());
        ModRecipes.init();


    }

    public void init(FMLInitializationEvent e){

    }

    public void postInit(FMLPostInitializationEvent e){
        }



    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        ModBlocks.preInit();


    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
    }

    @SubscribeEvent
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {

    }









}
