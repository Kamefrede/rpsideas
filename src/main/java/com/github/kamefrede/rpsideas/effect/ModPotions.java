package com.github.kamefrede.rpsideas.effect;


import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ModPotions {
    public static Potion psishock;
    public static Potion psipulse;

    //TODO brewing recipes!

    public static void init() {
        psishock = new PotionPsishock();
        psipulse = new PotionPsipulse();

    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> evt) {
        evt.getRegistry().register(psishock);
        evt.getRegistry().register(psipulse);
    }

}
