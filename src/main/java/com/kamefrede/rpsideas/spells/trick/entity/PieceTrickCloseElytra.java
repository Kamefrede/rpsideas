package com.kamefrede.rpsideas.spells.trick.entity;


import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;


public class PieceTrickCloseElytra extends PieceTrick {

    private SpellParam num;

    public PieceTrickCloseElytra(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(num = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double value = SpellHelpers.getNumber(this, context, num, 1);
        if (Math.abs(value) < 1.0) context.caster.setFlag(7, false);

        return null;
    }

}
