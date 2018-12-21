package com.kamefrede.rpsideas.crafting.factory;

import com.google.gson.JsonObject;
import com.kamefrede.rpsideas.util.RPSConfigHandler;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

public class MagazineConditionFactory implements IConditionFactory {

    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        boolean value = JsonUtils.getBoolean(json, "value", true);
        return () -> RPSConfigHandler.enableMagazine == value;
    }
}
