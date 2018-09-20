package com.github.kamefrede.rpsideas.proxy;

import com.github.kamefrede.rpsideas.items.ModItems;
import com.github.kamefrede.rpsideas.network.MessageParticleTrail;
import com.github.kamefrede.rpsideas.network.MessageSpamlessChat;
import com.github.kamefrede.rpsideas.network.RPSPacketHandler;
import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import vazkii.psi.client.core.handler.HUDHandler;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent e){
        super.preInit(e);
        OBJLoader.INSTANCE.addDomain(Reference.MODID);
        MinecraftForge.EVENT_BUS.register(HUDHandler.class);

    }

    @Override
    public void init(FMLInitializationEvent e){
        super.init(e);

    }

    @SubscribeEvent
    public static void models(ModelRegistryEvent e) {
        setDefaultModel(ModItems.flashRing);

    }

    private static void setDefaultModel(Item i) {
        setDefaultModel(i, 0);
    }

    private static void setDefaultModel(Item i, int damage) {
        ModelResourceLocation mrl = new ModelResourceLocation(i.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(i, damage, mrl);
    }

}
