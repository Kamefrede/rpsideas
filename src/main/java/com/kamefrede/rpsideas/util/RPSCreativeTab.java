package com.kamefrede.rpsideas.util;

import com.google.common.collect.Lists;
import com.kamefrede.rpsideas.RPSIdeas;
import com.kamefrede.rpsideas.items.RPSItems;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class RPSCreativeTab extends CreativeTabs {
    public static final RPSCreativeTab INSTANCE = new RPSCreativeTab();

    private RPSCreativeTab() {
        super(RPSIdeas.MODID);
        setNoTitle();
        setBackgroundImageName("psideas.png");
    }

    private static final List<Item> order = Lists.newArrayList();

    public static void set(Block block) {
        block.setCreativeTab(INSTANCE);
        Item item = Item.getItemFromBlock(block);
        if (item != Items.AIR)
            order.add(item);
    }

    public static void set(Item item) {
        item.setCreativeTab(INSTANCE);
        order.add(item);
    }

    @Override
    public void displayAllRelevantItems(@Nonnull NonNullList<ItemStack> stacks) {
        for (Item item : order)
            item.getSubItems(this, stacks);
        for (Item item : Item.REGISTRY)
            if (!order.contains(item))
                item.getSubItems(this, stacks);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @Nonnull
    public ItemStack createIcon() {
        return new ItemStack(RPSItems.flashRing);
    }

    @Override
    public boolean hasSearchBar() {
        return true;
    }
}
