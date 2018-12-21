package com.kamefrede.rpsideas.items.base;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.util.RPSCreativeTab;
import vazkii.arl.item.ItemMod;

/**
 * @author WireSegal
 * Created at 9:19 AM on 12/16/18.
 */
public class RPSItem extends ItemMod {
    public RPSItem(String name, String... variants) {
        super(name, variants);
        RPSCreativeTab.set(this);
    }

    @Override
    public String getModNamespace() {
        return RPSIdeas.MODID;
    }
}
