package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import vazkii.arl.item.ItemMod;

public class ItemGaussBullet extends ItemMod {
    public ItemGaussBullet() {
        super(LibItems.ITEM_GAUSS_BULLET);
        setCreativeTab(RPSCreativeTab.INST);
        setMaxStackSize(64);
    }

    @Override
    public String getModNamespace() {
        return Reference.MODID;
    }
}
