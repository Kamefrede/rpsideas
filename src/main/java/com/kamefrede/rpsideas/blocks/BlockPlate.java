package com.kamefrede.rpsideas.blocks;

import com.kamefrede.rpsideas.util.RPSCreativeTab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import vazkii.arl.interf.IBlockColorProvider;

import javax.annotation.Nonnull;

public class BlockPlate extends RPSBlock implements IBlockColorProvider {

    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public BlockPlate(String name) {
        super(name, Material.IRON, makeVariants(name));
        setLightLevel(1f);
        this.setHardness(5.0f);
        this.setResistance(10.0f);
        this.blockSoundType = SoundType.METAL;
    }

    private static String[] makeVariants(String name) {
        String[] array = new String[16];
        for (int i = 0; i < array.length; i++)
            array[i] = name + "_" + EnumDyeColor.byMetadata(i).getName();
        return array;
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COLOR);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COLOR).getMetadata();
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Nonnull
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.getBlockColor(state.getValue(COLOR));
    }

    @Override
    public IBlockColor getBlockColor() {
        return (IBlockState iBlockState, IBlockAccess access, BlockPos pos, int i) -> i == 1 ? MapColor.getBlockColor(iBlockState.getValue(COLOR)).colorValue : -1;
    }

    @Override
    public IItemColor getItemColor() {
        return (ItemStack stack, int index) -> MapColor.getBlockColor(EnumDyeColor.byMetadata(stack.getItemDamage())).colorValue;
    }

}
