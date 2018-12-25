package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.RPSItems;
import com.teamwizardry.librarianlib.features.base.ModCreativeTab;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RPSCreativeTab extends ModCreativeTab {

    public RPSCreativeTab() {
        super();
        setNoTitle();
        setBackgroundImageName(RPSIdeas.MODID + ".png");
    }

    @NotNull
    @Override
    public ItemStack getIconStack() {
        return new ItemStack(RPSItems.flashRing);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
