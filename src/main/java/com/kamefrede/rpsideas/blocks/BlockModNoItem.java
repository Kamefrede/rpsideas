package com.kamefrede.rpsideas.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.interf.IModBlock;
import vazkii.arl.util.ProxyRegistry;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 7:14 PM on 12/20/18.
 */
public abstract class BlockModNoItem extends Block implements IModBlock {

    private final String[] variants;
    private final String bareName;

    public BlockModNoItem(String name, Material materialIn, String... variants) {
        super(materialIn);

        if(variants.length == 0)
            variants = new String[] { name };

        bareName = name;
        this.variants = variants;

        if(registerInConstruction())
            setTranslationKey(name);
    }

    @Nonnull
    @Override
    public Block setTranslationKey(@Nonnull String name) {
        super.setTranslationKey(name);
        setRegistryName(getPrefix() + name);
        ProxyRegistry.register(this);
        return this;
    }

    public boolean registerInConstruction() {
        return true;
    }

    @Override
    public String getBareName() {
        return bareName;
    }

    @Override
    public String[] getVariants() {
        return variants;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemMeshDefinition getCustomMeshDefinition() {
        return null;
    }

    @Override
    public EnumRarity getBlockRarity(ItemStack stack) {
        return EnumRarity.COMMON;
    }

    @Override
    public IProperty[] getIgnoredProperties() {
        return new IProperty[0];
    }

    @Override
    public IProperty getVariantProp() {
        return null;
    }

    @Override
    public Class getVariantEnum() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public void registerStateMapper() {
        IStateMapper mapper = getStateMapper();
        IProperty[] ignored = getIgnoredProperties();
        if (mapper != null || ignored != null && ignored.length > 0) {
            if (mapper != null)
                ModelLoader.setCustomStateMapper(this, mapper);
            else
                ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(ignored).build());

        }
    }

}
