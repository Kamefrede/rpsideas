package com.kamefrede.rpsideas.compat.jei;

import com.kamefrede.rpsideas.items.RPSItems;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import vazkii.psi.common.item.base.ModItems;

/**
 * @author WireSegal
 * Created at 1:30 PM on 12/21/18.
 */
@JEIPlugin
public class JEICompat implements IModPlugin {

    public static IJeiHelpers helpers;

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
        subtypeRegistry.useNbtForSubtypes(ModItems.cad, RPSItems.cadMagazine);
    }
}
