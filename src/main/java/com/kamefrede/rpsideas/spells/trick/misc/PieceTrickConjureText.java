package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.entity.EntityConjuredText;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.Psi;

public class PieceTrickConjureText extends PieceTrick {

    private SpellParam text;
    private SpellParam position;
    private SpellParam time;

    public PieceTrickConjureText(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
        addParam(text = new ParamAny(SpellParams.GENERIC_NAME_TEXT, SpellParam.BLUE, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.CYAN, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        Double timeAlive = this.getParamEvaluation(time);
        if(timeAlive <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 pos = this.getParamValue(context, position);
        Object targetVal = this.getParamValue(context, text);
        Double maxTimeAlive =  this.getParamValue(context, time);
        if(pos == null) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if(!context.isInRadius(pos)) throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        String s = "null";
        if(maxTimeAlive > 3600)
            maxTimeAlive = 3600.0;
        if(targetVal != null)
            s = targetVal.toString();
        if(s.length() > 32)
            throw new SpellRuntimeException(SpellRuntimeExceptions.TEXT);

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
        EntityConjuredText conjuredText = new EntityConjuredText(context.caster.world);
        if(!context.caster.world.isRemote){
            conjuredText.setInfo(context.caster, colorizer, s, pos, maxTimeAlive.intValue());
            conjuredText.getEntityWorld().spawnEntity(conjuredText);
        }


        return true;
    }
}
