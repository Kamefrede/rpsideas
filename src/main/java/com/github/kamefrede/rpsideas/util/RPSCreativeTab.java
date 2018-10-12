package com.github.kamefrede.rpsideas.util;

import com.github.kamefrede.rpsideas.items.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RPSCreativeTab extends CreativeTabs {
    public static final RPSCreativeTab INST = new RPSCreativeTab();

    private RPSCreativeTab() {
        super(Reference.MODID);
        setNoTitle();
        setBackgroundImageName("psideas.png");
    }
    

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.flashRing);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
