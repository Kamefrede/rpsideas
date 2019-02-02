package com.kamefrede.rpsideas.spells.trick.misc;

import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

import static com.teamwizardry.librarianlib.features.kotlin.CommonUtilMethods.sendSpamlessMessage;

public class PieceTrickDebugSpamless extends PieceTrick {

    private SpellParam targetParam;
    private SpellParam numParam;

    public PieceTrickDebugSpamless(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(targetParam = new ParamAny(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
        addParam(numParam = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.RED, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        // NO-OP
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {

        Double numberVal = this.getParamValue(context, numParam);
        Object targetVal = getParamValue(context, targetParam);

        int number = -1;

        String s = "null";
        if (targetVal != null)
            s = targetVal.toString();

        if (numberVal != null) {
            String numStr = "" + numberVal;
            if (numberVal - numberVal.intValue() == 0) {
                int numInt = numberVal.intValue();
                numStr = "" + numInt;
                number = numInt;
            }

            s = TextFormatting.AQUA + "[" + numStr + "] " + TextFormatting.RESET + s;
        }

        TextComponentString component = new TextComponentString(s);
        sendSpamlessMessage(context.caster, component, "rps spamless".hashCode() + number);

        return null;
    }
}
