package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickEraseLiquid extends PieceTrick {

    private SpellParam position;

    public PieceTrickEraseLiquid(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 15);
        meta.addStat(EnumSpellStat.COST, 25);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        BlockPos pos = SpellHelpers.getBlockPos(this, context, position);

        World world = context.caster.world;
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (state.getMaterial().isLiquid()) {
            world.setBlockToAir(pos);
        }

        return null;
    }
}
