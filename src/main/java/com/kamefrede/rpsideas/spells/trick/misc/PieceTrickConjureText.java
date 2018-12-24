package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.entity.EntityConjuredText;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.cad.ICADColorizer;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.Psi;

public class PieceTrickConjureText extends PieceTrick {

    private SpellParam text;
    private SpellParam position;

    public PieceTrickConjureText(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.RED, false, false));
        addParam(text = new ParamAny(SpellParams.GENERIC_NAME_TEXT, SpellParam.BLUE, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 pos = this.getParamValue(context, position);
        Object targetVal = this.getParamValue(context, text);
        if(pos == null) throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        if(!context.isInRadius(pos)) throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        String s = "null";
        if(targetVal != null)
            s = targetVal.toString();
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
        EntityConjuredText conjuredText = new EntityConjuredText(context.caster.world);
        conjuredText.setInfo(context.caster, colorizer, s, pos);
        conjuredText.getEntityWorld().spawnEntity(conjuredText);

        return true;
    }
}
