package com.github.kamefrede.rpsideas.compat.botania;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fml.common.Loader;

import java.util.function.BooleanSupplier;

public class BotaniaRecipeConditionFactory implements IConditionFactory {
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        return ()  -> Loader.isModLoaded("botania");
    }
}
