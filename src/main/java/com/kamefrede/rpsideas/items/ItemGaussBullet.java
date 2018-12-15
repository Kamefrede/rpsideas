package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import vazkii.arl.item.ItemMod;

public class ItemGaussBullet extends ItemMod { // TODO: 12/15/18 look at
    public ItemGaussBullet() {
        super(LibItemNames.ITEM_GAUSS_BULLET);
        setCreativeTab(RPSCreativeTab.INSTANCE);
        setMaxStackSize(64);
    }

    @Override
    public String getModNamespace() {
        return RPSIdeas.MODID;
    }
}
