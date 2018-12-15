package com.kamefrede.rpsideas.items.base;

import com.kamefrede.rpsideas.util.RPSCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class Item16Colors extends ItemBlock {
    public Item16Colors(Block block) {
        super(block);

        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (tab == RPSCreativeTab.INSTANCE) {
            for (int i = 0; i < 16; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }


    @Nonnull
    @Override
    public String getUnlocalizedNameInefficiently(@Nonnull ItemStack stack) {
        return super.getUnlocalizedNameInefficiently(stack) + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getTranslationKey();
    }

}
