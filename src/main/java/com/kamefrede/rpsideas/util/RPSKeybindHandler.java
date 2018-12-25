package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;


@SideOnly(Side.CLIENT)
public class RPSKeybindHandler {

    public static final RPSKeybind[] keybinds = new RPSKeybind[12];
    public static RPSKeybind offhandCast;

    private static final String categoryName = "key.category." + RPSIdeas.MODID ;

    public static void init(){
        offhandCast = new RPSKeybind("rpsideas.kb.offhandcast", Keyboard.KEY_NONE, categoryName);
        keybinds[0] = new RPSKeybind("rpsideas.kb.switchslot01", Keyboard.KEY_NONE, categoryName);
        keybinds[1] = new RPSKeybind("rpsideas.kb.switchslot02", Keyboard.KEY_NONE, categoryName);
        keybinds[2] = new RPSKeybind("rpsideas.kb.switchslot03", Keyboard.KEY_NONE, categoryName);
        keybinds[3] = new RPSKeybind("rpsideas.kb.switchslot04", Keyboard.KEY_NONE, categoryName);
        keybinds[4] = new RPSKeybind("rpsideas.kb.switchslot05", Keyboard.KEY_NONE, categoryName);
        keybinds[5] = new RPSKeybind("rpsideas.kb.switchslot06", Keyboard.KEY_NONE, categoryName);
        keybinds[6] = new RPSKeybind("rpsideas.kb.switchslot07", Keyboard.KEY_NONE, categoryName);
        keybinds[7] = new RPSKeybind("rpsideas.kb.switchslot08", Keyboard.KEY_NONE, categoryName);
        keybinds[8] = new RPSKeybind("rpsideas.kb.switchslot09", Keyboard.KEY_NONE, categoryName);
        keybinds[9] = new RPSKeybind("rpsideas.kb.switchslot10", Keyboard.KEY_NONE, categoryName);
        keybinds[10] = new RPSKeybind("rpsideas.kb.switchslot11", Keyboard.KEY_NONE, categoryName);
        keybinds[11] = new RPSKeybind("rpsideas.kb.switchslot12", Keyboard.KEY_NONE, categoryName);
    }



}
