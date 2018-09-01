package com.github.kamefrede.psiaddon.proxy;

import com.github.kamefrede.psiaddon.Config;
import com.github.kamefrede.psiaddon.capability.stasis.damage.DefaultStasisDamageHandler;
import com.github.kamefrede.psiaddon.capability.stasis.damage.IStasisDamage;
import com.github.kamefrede.psiaddon.capability.stasis.damage.StasisDamageProvider;
import com.github.kamefrede.psiaddon.capability.stasis.damage.StasisDamageStorage;
import com.github.kamefrede.psiaddon.capability.stasis.time.DefaultStasisTimeHandler;
import com.github.kamefrede.psiaddon.capability.stasis.time.IStasisTime;
import com.github.kamefrede.psiaddon.capability.stasis.time.StasisTimeProvider;
import com.github.kamefrede.psiaddon.capability.stasis.time.StasisTimeStorage;
import com.github.kamefrede.psiaddon.spells.base.SpellPieces;
import com.github.kamefrede.psiaddon.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.*;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.AttachCapabilitiesEvent;
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
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "psiam.cfg"));
        Config.readConfig();
        SpellPieces.init();
        CapabilityManager.INSTANCE.register(IStasisDamage.class, new StasisDamageStorage(), DefaultStasisDamageHandler.class);
        CapabilityManager.INSTANCE.register(IStasisTime.class, new StasisTimeStorage(), DefaultStasisTimeHandler.class);


    }

    public void init(FMLInitializationEvent e){

        MinecraftForge.EVENT_BUS.register(new DefaultStasisDamageHandler());
        MinecraftForge.EVENT_BUS.register(new DefaultStasisTimeHandler());
    }

    public void postInit(FMLPostInitializationEvent e){
        if (config.hasChanged()){
            config.save();
        }

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){


    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){

    }


    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {

        if (event.getObject() instanceof EntityLiving){
            event.addCapability(new ResourceLocation(Reference.MODID, "stasis.damage.capability"), new StasisDamageProvider());
            event.addCapability(new ResourceLocation(Reference.MODID, "stasis.time.capability"), new StasisTimeProvider());
            System.out.println("attached capability to " + event.getObject());
        }
        if (event.getObject() instanceof EntityFallingBlock || event.getObject() instanceof EntityMinecart || event.getObject() instanceof EntityBoat || event.getObject() instanceof EntityArrow || event.getObject() instanceof EntityTNTPrimed || event.getObject() instanceof EntityItem){
            event.addCapability(new ResourceLocation(Reference.MODID, "stasis.time.capability"), new StasisTimeProvider());
        }

    }




}
