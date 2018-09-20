package com.github.kamefrede.rpsideas.items;

import com.github.kamefrede.rpsideas.util.Reference;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
    @GameRegistry.ObjectHolder(Reference.MODID + ":" + "flash_ring")
    public static final Item flashRing = Items.AIR;

    public static void register(IForgeRegistry<Item> reg) {
        reg.register(createItem(new ItemFlashRing(), "flash_ring"));
    }

    static <I extends Item> I createItem(I item, String name) {
        return createItem(item, name, true);
    }

    static <I extends Item> I createItem(I item, String name, boolean showInCreative) {
        ResourceLocation res = new ResourceLocation(Reference.MODID, name);

        item.setRegistryName(res);
        item.setUnlocalizedName(res.getResourceDomain() + "." + res.getResourcePath());



        return item;
    }

    static <IB extends ItemBlock> IB createItemBlock(IB itemBlock) {
        return createItemBlock(itemBlock, true);
    }

    static <IB extends ItemBlock> IB createItemBlock(IB itemBlock, boolean showInCreative) {
        ResourceLocation res = itemBlock.getBlock().getRegistryName();

        itemBlock.setRegistryName(res);


        return itemBlock;
    }
}
