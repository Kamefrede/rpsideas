package com.github.kamefrede.rpsideas.util;


import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

//thanks quat
public class RPSSoundHandler {
    public static final SoundEvent THWACK = makeSoundEvent("thwack");

    //TODO make ur own sound events!
    public static final SoundEvent CAD_CASE_OPEN = SoundEvents.BLOCK_WOODEN_DOOR_OPEN;
    public static final SoundEvent CAD_CASE_CLOSE = SoundEvents.BLOCK_WOODEN_DOOR_OPEN;

    //shamelessly stolen from botania \:D/
    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation loc = new ResourceLocation(Reference.MODID, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    public static void registerSounds(IForgeRegistry<SoundEvent> reg) {
        reg.register(THWACK);
    }
}
