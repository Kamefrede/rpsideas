package com.kamefrede.rpsideas.items;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import com.kamefrede.rpsideas.util.libs.LibItemNames;
import vazkii.arl.item.ItemMod;

public class ItemBraceletCAD extends ItemMod { // TODO: 12/15/18 look at
    public ItemBraceletCAD() {
        super(LibItemNames.BRACELET_CAD);
        setMaxStackSize(1);
        setCreativeTab(RPSCreativeTab.INSTANCE);
    }


    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public String getModNamespace() {
        return RPSIdeas.MODID;
    }
}
