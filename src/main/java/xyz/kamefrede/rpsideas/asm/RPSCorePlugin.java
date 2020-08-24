package com.kamefrede.rpsideas.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

//-Dfml.coreMods.load=com.kamefrede.rpsideas.asm.RPSCorePlugin

@IFMLLoadingPlugin.Name("RPSIdeas Plugin")
@IFMLLoadingPlugin.TransformerExclusions("com.kamefrede.rpsideas.asm")
@IFMLLoadingPlugin.SortingIndex(1001) // After runtime deobfuscation
public class RPSCorePlugin implements IFMLLoadingPlugin {

    public static boolean runtimeDeobf = false;

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{
                "com.kamefrede.rpsideas.asm.RPSTransformer"
        };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        runtimeDeobf = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
