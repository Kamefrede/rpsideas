package com.kamefrede.rpsideas.spells.operator.block;

import com.google.common.base.Predicate;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

import javax.annotation.Nullable;
import java.util.List;

public class PieceOperatorGetBlockComparatorStrength extends PieceOperator {// TODO: 12/15/18 look at


    SpellParam axisParam;
    SpellParam target;

    public PieceOperatorGetBlockComparatorStrength(Spell spell) {
        super(spell);
    }


    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, true, false));
        addParam(axisParam = new ParamVector(SpellParams.GENERIC_NAME_VECTOR, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }


    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 ax = this.<Vector3>getParamValue(context, axisParam);
        EnumFacing whichWay;
        Vector3 vec = this.<Vector3>getParamValue(context, target);
        if (vec == null || vec.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        if (ax == null || ax.isZero()) {
            whichWay = EnumFacing.UP;
        } else if (!ax.isAxial()) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NON_AXIAL_VECTOR);
        } else {
            whichWay = EnumFacing.getFacingFromVector((float) ax.x, (float) ax.y, (float) ax.z);
        }

        BlockPos pos = new BlockPos(vec.x, vec.y, vec.z);
        IBlockState state = context.caster.world.getBlockState(pos);


        return calculateOutput(context.caster.world, pos, state, whichWay);
    }

    private int calculateOutput(World worldIn, BlockPos pos, IBlockState state, EnumFacing facing) {
        return calculateInputStrength(worldIn, pos, state, facing);
    }

    private int calc1(World worldIn, BlockPos pos, IBlockState state, EnumFacing enumfacing) {
        int i = worldIn.getRedstonePower(pos, enumfacing);

        if (i >= 15) {
            return i;
        } else {
            return Math.max(i, state.getBlock() == Blocks.REDSTONE_WIRE ? state.getValue(BlockRedstoneWire.POWER) : 0);
        }
    }

    private int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state, EnumFacing enumfacing) {
        int i = calc1(worldIn, pos, state, enumfacing);


        if (state.hasComparatorInputOverride()) {
            i = state.getComparatorInputOverride(worldIn, pos);
        } else if (i < 15 && state.isNormalCube()) {

            if (state.hasComparatorInputOverride()) {
                i = state.getComparatorInputOverride(worldIn, pos);
            } else if (state.getMaterial() == Material.AIR) {
                EntityItemFrame entityitemframe = this.findItemFrame(worldIn, enumfacing, pos);

                if (entityitemframe != null) {
                    i = entityitemframe.getAnalogOutput();
                }
            }
        }

        return i;
    }

    @Nullable
    private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos) {
        List<EntityItemFrame> list = worldIn.<EntityItemFrame>getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), (double) (pos.getX() + 1), (double) (pos.getY() + 1), (double) (pos.getZ() + 1)), new Predicate<Entity>() {
            public boolean apply(@Nullable Entity p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_.getHorizontalFacing() == facing;
            }
        });
        return list.size() == 1 ? (EntityItemFrame) list.get(0) : null;
    }


    @Override
    public Class<Double> getEvaluationType() {
        return Double.class;
    }
}
