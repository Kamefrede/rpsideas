package com.kamefrede.rpsideas.spells.trick.misc;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickLabelDebug extends PieceTrick {

    private SpellParam targetParam;

    public PieceTrickLabelDebug(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(targetParam = new ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        // NO-OP
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {

        Object targetVal = getParamValue(context, targetParam);

        String label = this.comment;

        String s = "null";
        if (targetVal != null)
            s = targetVal.toString();

        if (!label.isEmpty())
            s = TextFormatting.AQUA + "[" + label + "] " + TextFormatting.RESET + s;

        TextComponentString component = new TextComponentString(s);
        context.caster.sendStatusMessage(component, false);

        return null;
    }
}
