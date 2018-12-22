package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;


public class RPSKeybindHandler {

    public static final KeyBinding[] keyBindings = new KeyBinding[12];
    public static KeyBinding offhandCast;

    private static final String categoryName = "key.category." + RPSIdeas.MODID ;

    public static void init(){
        offhandCast = new KeyBinding("rpsideas.kb.offhandcast", Keyboard.KEY_NONE, categoryName);
        ClientRegistry.registerKeyBinding(offhandCast);
        keyBindings[0] = new KeyBinding("rpsideas.kb.switchslot1", Keyboard.KEY_NONE, categoryName);
        keyBindings[1] = new KeyBinding("rpsideas.kb.switchslot2", Keyboard.KEY_NONE, categoryName);
        keyBindings[2] = new KeyBinding("rpsideas.kb.switchslot3", Keyboard.KEY_NONE, categoryName);
        keyBindings[3] = new KeyBinding("rpsideas.kb.switchslot4", Keyboard.KEY_NONE, categoryName);
        keyBindings[4] = new KeyBinding("rpsideas.kb.switchslot5", Keyboard.KEY_NONE, categoryName);
        keyBindings[5] = new KeyBinding("rpsideas.kb.switchslot6", Keyboard.KEY_NONE, categoryName);
        keyBindings[6] = new KeyBinding("rpsideas.kb.switchslot7", Keyboard.KEY_NONE, categoryName);
        keyBindings[7] = new KeyBinding("rpsideas.kb.switchslot8", Keyboard.KEY_NONE, categoryName);
        keyBindings[8] = new KeyBinding("rpsideas.kb.switchslot9", Keyboard.KEY_NONE, categoryName);
        keyBindings[9] = new KeyBinding("rpsideas.kb.switchslot10", Keyboard.KEY_NONE, categoryName);
        keyBindings[10] = new KeyBinding("rpsideas.kb.switchslot11", Keyboard.KEY_NONE, categoryName);
        keyBindings[11] = new KeyBinding("rpsideas.kb.switchslot12", Keyboard.KEY_NONE, categoryName);

        for (KeyBinding keyBinding : keyBindings)
            ClientRegistry.registerKeyBinding(keyBinding);
    }



}
