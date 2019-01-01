package com.kamefrede.rpsideas.spells.trick.entity;

import com.kamefrede.rpsideas.entity.EntityFancyCircle;
import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
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
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
        addParam(direction = new ParamVector(SpellParams.GENERIC_NAME_DIRECTION, SpellParam.CYAN, true, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, true, true));
        addParam(scale = new ParamNumber(SpellParams.GENERIC_NAME_SCALE, SpellParam.GREEN, true, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);

        double scl = SpellHelpers.evaluateNumber(this, scale, 1);
        double tim = SpellHelpers.evaluateNumber(this, time, 100);

        if (scl > 4 || scl <= 0)
            throw new SpellCompilationException(SpellCompilationExceptions.SCALE, x, y);
        if (tim <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);

        meta.addStat(EnumSpellStat.POTENCY, (int) (scl * tim / 100));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 pos = this.getParamValue(context, position);
        Vector3 dir = this.getParamValue(context, direction);
        double scl = SpellHelpers.getNumber(this, context, scale, 1);
        double maxTimeAlive = SpellHelpers.getNumber(this, context, time, 100);

        if (pos == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if (dir == null || dir.isZero())
            dir = new Vector3(0, 1, 0);
        if (!context.isInRadius(pos))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

        World world = context.caster.world;
        if (!world.isRemote) {
            EntityFancyCircle circle = new EntityFancyCircle(world);
            circle.setInfo(context.caster, colorizer, pos, (int) maxTimeAlive, (float) scl, dir.toVec3D().normalize());
            circle.world.spawnEntity(circle);
        }
        return null;
    }
}
