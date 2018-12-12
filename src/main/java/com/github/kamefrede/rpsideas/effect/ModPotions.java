package com.github.kamefrede.rpsideas.effect;


import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class ModPotions {
    public static final Potion psishock = new PotionPsishock();
    public static final Potion psipulse = new PotionPsipulse();

    public static final PotionType psishockType = new PotionType(LibItems.PSISHOCK, new PotionEffect(psishock, 160));
    public static final PotionType psishockLongType = new PotionType(LibItems.PSISHOCK_LONG, new PotionEffect(psishock, 240));
    public static final PotionType psishockStrongType = new PotionType(LibItems.PSISHOCK_STRONG, new PotionEffect(psishock, 125, 1));

    public static final PotionType psipulseType = new PotionType(LibItems.PSIPULSE, new PotionEffect(psipulse, 400));
    public static final PotionType psipulseStrongType = new PotionType(LibItems.PSIPULSE_STRONG, new PotionEffect(psipulse, 250, 1));
    public static final PotionType psipulseLongType = new PotionType(LibItems.PSIPULSE_LONG, new PotionEffect(psipulse, 600));




    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> evt) {
        evt.getRegistry().register(psishock);
        evt.getRegistry().register(psipulse);
    }

    @SubscribeEvent
    public static void registerPotionType(RegistryEvent.Register<PotionType> evt){
        evt.getRegistry().register(psishockType);
        evt.getRegistry().register(psishockLongType);
        evt.getRegistry().register(psishockStrongType);
        evt.getRegistry().register(psipulseType);
        evt.getRegistry().register(psipulseLongType);
        evt.getRegistry().register(psipulseStrongType);
    }

}
