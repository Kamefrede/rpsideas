package xyz.kamefrede.rpsideas.util;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RPSKeybind extends KeyBinding {
    public RPSKeybind(String description, int keyCode, String category) {
        super(description, keyCode, category);
        ClientRegistry.registerKeyBinding(this);
    }

    @Override
    public int compareTo(KeyBinding other) {
        if (this.getKeyCategory().equals(other.getKeyCategory()))
            return this.getKeyDescription().compareTo(other.getKeyDescription());
        return super.compareTo(other);
    }
}
