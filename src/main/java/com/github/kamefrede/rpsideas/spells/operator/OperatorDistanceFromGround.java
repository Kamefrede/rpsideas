package com.github.kamefrede.rpsideas.spells.operator;

import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.piece.PieceOperator;

public class OperatorDistanceFromGround extends PieceOperator {

    SpellParam target;

    public OperatorDistanceFromGround(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double maxheight = 256;
        Entity targetVal = this.<Entity>getParamValue(context, target);
        if(targetVal == null) throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        if(targetVal.onGround) return 0.0D;
        if(context.caster.posY <= 256) maxheight = context.caster.posY - context.caster.getYOffset();

        RayTraceResult distancefromground = context.caster.world.rayTraceBlocks(new Vec3d(context.caster.posX, maxheight, context.caster.posY), new Vec3d(0,-1,0), false, true, false);
        if(distancefromground == null) throw new SpellRuntimeException(SpellRuntimeExceptions.NO_GROUND);
        return context.caster.posY - distancefromground.getBlockPos().getY();



    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
