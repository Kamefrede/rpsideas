package com.kamefrede.rpsideas.util;


import com.kamefrede.rpsideas.RPSIdeas;
import com.teamwizardry.librarianlib.features.base.ModSoundEvent;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = RPSIdeas.MODID)
public class RPSSoundHandler {
    public static final SoundEvent THWACK = makeSoundEvent("thwack");

    public static final SoundEvent CAD_CASE_OPEN = SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
    public static final SoundEvent CAD_CASE_CLOSE = SoundEvents.BLOCK_WOODEN_DOOR_OPEN;

    private static SoundEvent makeSoundEvent(String name) {
        return new ModSoundEvent(name);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> e) {
        e.getRegistry().register(THWACK);
    }
}
