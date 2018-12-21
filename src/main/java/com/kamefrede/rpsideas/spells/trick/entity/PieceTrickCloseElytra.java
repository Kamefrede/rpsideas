package com.kamefrede.rpsideas.spells.trick.entity;


import vazkii.psi.api.spell.*;
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
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double value = this.<Double>getParamValue(context, num);
        if (Math.abs(value) < 1.0) context.caster.setFlag(7, false);

        return false;
    }

}
