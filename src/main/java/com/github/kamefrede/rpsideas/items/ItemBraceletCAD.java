package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import vazkii.arl.item.ItemMod;

public class ItemBraceletCAD extends ItemMod {
    public ItemBraceletCAD() {
        super(LibItems.BRACELET_CAD);
        setMaxStackSize(1);
        setCreativeTab(RPSCreativeTab.INST);
    }



    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public String getModNamespace() {
        return Reference.MODID;
    }
}
