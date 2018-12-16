package com.kamefrede.rpsideas.proxy;

import com.kamefrede.rpsideas.entity.RPSEntities;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.spells.base.SpellPieces;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

    @SuppressWarnings("unused")
    public void preInit(FMLPreInitializationEvent e) {
        SpellPieces.init();
        RPSPacketHandler.initPackets();
        RPSEntities.init();
    }

    public void init(FMLInitializationEvent e) {
        // NO-OP
    }

}
