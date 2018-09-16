package com.github.kamefrede.rpsideas.proxy;

import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e){
        super.preInit(e);
        OBJLoader.INSTANCE.addDomain(Reference.MODID);
    }

    @Override
    public void init(FMLInitializationEvent e){
        super.init(e);

    }

}
