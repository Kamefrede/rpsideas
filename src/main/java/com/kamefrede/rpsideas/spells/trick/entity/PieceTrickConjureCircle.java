package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.entity.EntityConjuredText;
import com.kamefrede.rpsideas.entity.EntityFancyCircle;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.Objects;

public class PieceTrickConjureCircle extends PieceTrick {

    private SpellParam time;
    private SpellParam position;
    private SpellParam scale;
    private SpellParam direction;

    public PieceTrickConjureCircle(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, false, true));
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION,SpellParam.RED, false, false));
        addParam(scale = new ParamNumber(SpellParams.GENERIC_NAME_SCALE, SpellParam.GREEN, false, false));
        addParam(direction = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.CYAN, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        Double scl = this.getParamEvaluation(scale);
        Double tim = this.getParamEvaluation(time);
        if(scl != null &&(scl > 1 || scl <= 0))
            throw new SpellCompilationException(SpellRuntimeExceptions.SCALE);
        if(tim != null && tim <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE);

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 pos = this.getParamValue(context, position);
        Vector3 dir = this.getParamValue(context, direction);
        double scl = this.getParamValue(context, scale);
        double maxTimeAlive = this.getParamValue(context, time);
        if (pos == null) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if(dir == null || dir.isZero()) dir = new Vector3(0,1,0);
        if (!context.isInRadius(pos))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

        World world = context.caster.world;
        if (!world.isRemote) {
            EntityFancyCircle circle = new EntityFancyCircle(world);
            circle.setInfo(context.caster, colorizer, pos, (int)maxTimeAlive,(float) scl,  dir.toVec3D());
            circle.getEntityWorld().spawnEntity(circle);
        }
        return null;
    }
}
