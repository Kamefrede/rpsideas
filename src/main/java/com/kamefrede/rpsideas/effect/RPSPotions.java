package com.kamefrede.rpsideas.effect;


import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSPotions {
    public static final Potion psishock = new PotionPsishock();
    public static final Potion psipulse = new PotionPsipulse();

    public static final PotionType psishockType = new PotionType(RPSIdeas.MODID + "." + LibItemNames.PSISHOCK, new PotionEffect(psishock, 160))
            .setRegistryName(new ResourceLocation(RPSIdeas.MODID, LibItemNames.PSISHOCK));
    public static final PotionType psishockLongType = new PotionType(RPSIdeas.MODID + "." + LibItemNames.PSISHOCK_LONG, new PotionEffect(psishock, 240))
            .setRegistryName(new ResourceLocation(RPSIdeas.MODID, LibItemNames.PSISHOCK_LONG));
    public static final PotionType psishockStrongType = new PotionType(RPSIdeas.MODID + "." + LibItemNames.PSISHOCK_STRONG, new PotionEffect(psishock, 125, 1))
            .setRegistryName(new ResourceLocation(RPSIdeas.MODID, LibItemNames.PSISHOCK_STRONG));

    public static final PotionType psipulseType = new PotionType(RPSIdeas.MODID + "." + LibItemNames.PSIPULSE, new PotionEffect(psipulse, 400))
            .setRegistryName(new ResourceLocation(RPSIdeas.MODID, LibItemNames.PSIPULSE));
    public static final PotionType psipulseStrongType = new PotionType(RPSIdeas.MODID + "." + LibItemNames.PSIPULSE_STRONG, new PotionEffect(psipulse, 250, 1))
            .setRegistryName(new ResourceLocation(RPSIdeas.MODID, LibItemNames.PSIPULSE_LONG));
    public static final PotionType psipulseLongType = new PotionType(RPSIdeas.MODID + "." + LibItemNames.PSIPULSE_LONG, new PotionEffect(psipulse, 600))
            .setRegistryName(new ResourceLocation(RPSIdeas.MODID, LibItemNames.PSIPULSE_STRONG));

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> evt) {
        evt.getRegistry().registerAll(psishock,
                psipulse);
    }

    @SubscribeEvent
    public static void registerPotionType(RegistryEvent.Register<PotionType> evt) {
        evt.getRegistry().registerAll(psishockType,
                psishockLongType,
                psishockStrongType,
                psipulseType,
                psipulseLongType,
                psipulseStrongType);
    }

}
