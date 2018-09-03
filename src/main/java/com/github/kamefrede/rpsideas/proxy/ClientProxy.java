package com.github.kamefrede.rpsideas.proxy;

import com.github.kamefrede.rpsideas.util.LibBlocks;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e){
        super.preInit(e);
        OBJLoader.INSTANCE.addDomain(Reference.MODID);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event){
        LibBlocks.initModels();
    }
}
