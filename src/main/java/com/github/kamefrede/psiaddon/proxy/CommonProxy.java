package com.github.kamefrede.psiaddon.proxy;

import com.github.kamefrede.psiaddon.blocks.ConjuredEtherealBlock;
import com.github.kamefrede.psiaddon.spells.base.SpellPieces;
import com.github.kamefrede.psiaddon.util.LibBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {

    //Config instance
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e){
        SpellPieces.init();


    }

    public void init(FMLInitializationEvent e){

    }

    public void postInit(FMLPostInitializationEvent e){
        if (config.hasChanged()){
            config.save();
        }

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        event.getRegistry().register(new ConjuredEtherealBlock());


    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        event.getRegistry().register(new ItemBlock(LibBlocks.conjuredEtherealBlock).setRegistryName(LibBlocks.conjuredEtherealBlock.getRegistryName()));;

    }







}
