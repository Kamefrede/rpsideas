package com.github.kamefrede.rpsideas.spells.trick.misc;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSupressNextTrick extends PieceTrick {

    SpellParam target;

    public PieceTrickSupressNextTrick(Spell spell) {
        super(spell);
    }


    @Override
    public void initParams() {
        addParam(target = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Double timeVal = this.<Double>getParamValue(context, target);
        if(Math.abs(timeVal) < 1){
            int index = context.actions.indexOf(context.cspell.currentAction);
            for(int i = index; i < context.actions.size(); i++){
                if(context.actions.get(i).piece instanceof PieceTrick ){
                    context.actions.remove(i);
                    break;
                }
            }
        }


        return null;
    }
}
