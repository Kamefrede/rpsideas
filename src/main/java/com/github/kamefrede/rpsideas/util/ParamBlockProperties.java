package com.github.kamefrede.rpsideas.util;

import net.minecraft.block.state.IBlockProperties;
import vazkii.psi.api.spell.param.ParamSpecific;

public class ParamBlockProperties extends ParamSpecific {

    public static final String GENERIC_NAME_PARAM = "rpsideas.spellparam.param";

    public ParamBlockProperties(String name, int color, boolean canDisable){
        super(name, color, canDisable, false);
    }

    @Override
    protected Class<?> getRequiredType(){
        return BlockProperties.class;
    }
}
