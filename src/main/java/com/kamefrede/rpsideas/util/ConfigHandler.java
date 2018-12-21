package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
@Config(modid = RPSIdeas.MODID, category = "crafting")
public class ConfigHandler {
    @Config.Name("enableInlineCaster")
    @Config.Comment("Is the Inline Caster craftable?")
    public static boolean enableInline = true;

    @Config.Name("enableCADCase")
    @Config.Comment("Is the CAD Case craftable?")
    public static boolean enableCase = true;

    @Config.Name("enableFlashRing")
    @Config.Comment("Is the Flash Ring craftable?")
    public static boolean enableRing = true;

    @Config.Name("enableMagazine")
    @Config.Comment("Is the Spell Magazine craftable?")
    public static boolean enableMagazine = true;

    @Config.Name("enableCreativeCAD")
    @Config.Comment("If Avaritia is present, should the creative CAD assembly be craftable?")
    public static boolean enableCreativeCAD = false;
}
