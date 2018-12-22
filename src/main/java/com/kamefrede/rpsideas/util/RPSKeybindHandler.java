package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;

public class RPSKeybindHandler {

    public static KeyBinding[] keyBindings;

    private static final String categoryName = "key.category." + RPSIdeas.MODID ;

    public static void init(){
        keyBindings[0] = new KeyBinding("rpsideas.kb.offhandcast", 0, categoryName);
        keyBindings[1] = new KeyBinding("rpsideas.kb.switchslot1", 0, categoryName);
        keyBindings[2] = new KeyBinding("rpsideas.kb.switchslot2", 0, categoryName);
        keyBindings[3] = new KeyBinding("rpsideas.kb.switchslot3", 0, categoryName);
        keyBindings[4] = new KeyBinding("rpsideas.kb.switchslot4", 0, categoryName);
        keyBindings[5] = new KeyBinding("rpsideas.kb.switchslot5", 0, categoryName);
        keyBindings[6] = new KeyBinding("rpsideas.kb.switchslot6", 0, categoryName);
        keyBindings[7] = new KeyBinding("rpsideas.kb.switchslot7", 0, categoryName);
        keyBindings[8] = new KeyBinding("rpsideas.kb.switchslot8", 0, categoryName);
        keyBindings[9] = new KeyBinding("rpsideas.kb.switchslot9", 0, categoryName);
        keyBindings[10] = new KeyBinding("rpsideas.kb.switchslot10", 0, categoryName);
        keyBindings[11] = new KeyBinding("rpsideas.kb.switchslot11", 0, categoryName);
        keyBindings[12] = new KeyBinding("rpsideas.kb.switchslot12", 0, categoryName);

        for (KeyBinding keyBinding : keyBindings)
            ClientRegistry.registerKeyBinding(keyBinding);
    }



}
