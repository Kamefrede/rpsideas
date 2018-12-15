package com.kamefrede.rpsideas.util;

import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class RPSCreativeTab extends CreativeTabs {
    public static final RPSCreativeTab INSTANCE = new RPSCreativeTab();

    private RPSCreativeTab() {
        super(RPSIdeas.MODID);
        setNoTitle();
        setBackgroundImageName("psideas.png");
    }


    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public ItemStack createIcon() {
        return new ItemStack(ModItems.flashRing);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
