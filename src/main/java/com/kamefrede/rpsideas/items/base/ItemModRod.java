package com.kamefrede.rpsideas.items.base;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.arl.interf.IVariantHolder;
import vazkii.arl.item.ItemMod;
import vazkii.arl.util.ProxyRegistry;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 10:06 AM on 12/16/18.
 */
public abstract class ItemModRod extends ItemFishingRod implements IVariantHolder {

    private final String bareName;

    public ItemModRod(String name) {
        super();
        setTranslationKey(name);
        bareName = name;
        ItemMod.variantHolders.add(this);
    }

    @Nonnull
    @Override
    public Item setTranslationKey(@Nonnull String name) {
        super.setTranslationKey(name);
        setRegistryName(new ResourceLocation(getPrefix() + name));
        ProxyRegistry.register(this);

        return this;
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack par1ItemStack) {
        par1ItemStack.getItemDamage();

        return "item." + getPrefix() + bareName;
    }

    @Override
    public String[] getVariants() {
        return new String[]{bareName};
    }

    @Override
    public ItemMeshDefinition getCustomMeshDefinition() {
        return null;
    }

}
