package com.github.kamefrede.rpsideas.blocks;

import com.github.kamefrede.rpsideas.util.RPSCreativeTab;
import com.github.kamefrede.rpsideas.util.Reference;
import com.github.kamefrede.rpsideas.util.libs.LibBlocks;
import com.github.kamefrede.rpsideas.util.libs.LibItems;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.block.BlockMetaVariants;
import vazkii.arl.block.BlockMod;
import vazkii.arl.interf.IBlockColorProvider;

public class BlockPlate extends BlockMod implements IBlockColorProvider {

    public static class Companion{
        public static String[] makeVariants(String name) {
            String[] array = new String[16];
            for (int i = 0; i < array.length; i++)
                array[i] = name + "_" + EnumDyeColor.byMetadata(i).getName();
            return array;
        }


    }
    public BlockPlate(String name){
        super(name, Material.IRON, Companion.makeVariants(name));
        setLightLevel(1f);
        this.setHardness(5.0f);
        this.setResistance(10.0f);
        setCreativeTab(RPSCreativeTab.INST);
        this.blockSoundType = SoundType.METAL;
    }

    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);


    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COLOR);
    }

    @Override
    public String[] getVariants() {
        return super.getVariants();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }


    @Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.getBlockColor(state.getValue(COLOR));
    }



    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockColor getBlockColor() {
        return (IBlockState iBlockState, IBlockAccess access, BlockPos pos, int i) -> i == 1 ? MapColor.getBlockColor(iBlockState.getValue(COLOR)).colorValue  : 0xFFFFFF;
    }

    @Override
    public IItemColor getItemColor() {
        return (ItemStack stack, int index) -> MapColor.getBlockColor(EnumDyeColor.byMetadata(stack.getItemDamage())).colorValue;
    }

    @Override
    public String getModNamespace() {
        return Reference.MODID;
    }



}
