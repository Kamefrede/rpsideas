package com.github.kamefrede.rpsideas.spells.trick.misc;

import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.Stack;

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
            Stack<CompiledSpell.Action> stack = (Stack<CompiledSpell.Action>) context.actions.clone();

            while (!stack.isEmpty()){
                CompiledSpell.Action a = stack.pop();
                if(a.piece instanceof PieceTrick && a != context.cspell.currentAction){
                    context.actions.remove(a);
                    break;
                }
            }
        }


        return null;
    }
}
