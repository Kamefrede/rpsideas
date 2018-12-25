package com.kamefrede.rpsideas.blocks;

import com.teamwizardry.librarianlib.features.base.block.BlockMod;
import com.teamwizardry.librarianlib.features.base.block.IBlockColorProvider;
import com.teamwizardry.librarianlib.features.base.block.IGlowingBlock;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function4;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlockPlate extends BlockMod implements IBlockColorProvider, IGlowingBlock {

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

    @Override
    @SideOnly(Side.CLIENT)
    public int packedGlowCoords(@NotNull IBlockAccess world, @NotNull IBlockState state, @NotNull BlockPos pos) {
        return 0xf000f0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldGlow(@NotNull IBlockAccess world, @NotNull BakedQuad quad, @NotNull IBlockState state, @NotNull BlockPos pos) {
        return quad.hasTintIndex() && quad.getTintIndex() == 1;
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

    @Nullable
    @Override
    public Function4<IBlockState, IBlockAccess, BlockPos, Integer, Integer> getBlockColorFunction() {
        return (IBlockState iBlockState, IBlockAccess access, BlockPos pos, Integer i) -> i == 1 ? MapColor.getBlockColor(iBlockState.getValue(COLOR)).colorValue : -1;
    }

    @Nullable
    @Override
    public Function2<ItemStack, Integer, Integer> getItemColorFunction() {
        return (ItemStack stack, Integer index) -> MapColor.getBlockColor(EnumDyeColor.byMetadata(stack.getItemDamage())).colorValue;
    }

}
