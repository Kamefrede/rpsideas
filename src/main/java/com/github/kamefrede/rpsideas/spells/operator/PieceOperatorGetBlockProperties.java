package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.util.BlockProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetBlockProperties extends PieceOperator {

    SpellParam position;

    public PieceOperatorGetBlockProperties(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.YELLOW, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 pos = this.<Vector3>getParamValue(context, position);
        BlockPos poss = new BlockPos(pos.toVec3D());
        World world = context.focalPoint.world;
        if(pos == null){
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }
        if(!context.isInRadius(pos)) {
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        }

        return new BlockProperties(world.getBlockState(poss), poss, world);
    }

    @Override
    public Class<?> getEvaluationType() {
        return BlockProperties.class;
    }
}
