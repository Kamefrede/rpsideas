package com.kamefrede.rpsideas.proxy;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber
public class ConfigHandler {// TODO: 12/15/18 look at
    public static boolean enableInline;
    public static boolean enableCase;
    public static boolean enableRing;
    public static boolean enableCreativeCAD;

    private static Configuration config;

    static void initConfig(File configFile) {
        config = new Configuration(configFile);

        processConfig();
    }

    static void processConfig() {
        enableInline = isEnabled("enableInlineCaster", "Is the Inline Caster craftable?", true);
        enableCase = isEnabled("enableCADCase", "Is the CAD Case craftable?", true);
        enableRing = isEnabled("enableFlashRing", "Is the Flash Ring craftable?", true);
        enableCreativeCAD = isEnabled("enableCreativeCAD", "If Avaritia is present should the creative CAD assembly be craftable?", true);

        if (config.hasChanged()) config.save();
    }

    public static boolean isEnabled(String configName, String description, boolean def) {
        return config.getBoolean(configName, "general", def, description, RPSIdeas.MODID + ".config." + configName);
    }

    @SubscribeEvent
    public static void changed(ConfigChangedEvent e) {
        if (e.getModID().equals(RPSIdeas.MODID)) {
            processConfig();
        }
    }
}
