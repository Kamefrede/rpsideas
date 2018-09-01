package com.github.kamefrede.psiaddon;

import com.github.kamefrede.psiaddon.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

public class Config {

    public static final String CATEGORY_GENERAL = "general";

    // Values can be acessed elsewhere in the mod:
    public static boolean debug1 = true;
    public static String debugString = "Lorem";

    public static void readConfig(){
        Configuration cfg = CommonProxy.config;
        try {
            cfg.load();
            initGeneralConfig(cfg);
        } catch ( Exception e1){
            Psiam.logger.log(Level.ERROR, "Problem loading config file", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    public static void initGeneralConfig(Configuration cfg){
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Configuration");
        debug1 = cfg.getBoolean("debug boolean",  CATEGORY_GENERAL, debug1, "Toggle for a debug action");
        debugString = cfg.getString("debug string", CATEGORY_GENERAL, debugString, "Set a debug string");
    }

}
