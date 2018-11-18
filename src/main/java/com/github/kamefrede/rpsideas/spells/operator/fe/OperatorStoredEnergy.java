package com.github.kamefrede.rpsideas.spells.operator.fe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class OperatorStoredEnergy extends PieceOperator {

    SpellParam position;


    public OperatorStoredEnergy(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.COMPLEXITY, 2);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 posVec = this.<Vector3>getParamValue(context, position);
        if(context.caster.world.isRemote) return null;
        if(posVec == null || posVec.isZero()) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        BlockPos pos = new BlockPos(posVec.x, posVec.y, posVec.z);
        World world = context.caster.world;
        IBlockState state = context.caster.world.getBlockState(pos);
        Block block = state.getBlock();
        if(!block.hasTileEntity(state)) return 0.0D;
        TileEntity tileent = world.getTileEntity(pos);
        assert tileent != null;
        IEnergyStorage storage = FEHelper.checkIfCapabilityExists(tileent);
        if(storage == null) return 0.0D;

        return storage.getEnergyStored();
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
