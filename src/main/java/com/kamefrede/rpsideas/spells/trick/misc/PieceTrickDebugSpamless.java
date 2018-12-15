package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.network.MessageSpamlessChat;
import com.kamefrede.rpsideas.network.RPSPacketHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamAny;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickDebugSpamless extends PieceTrick {// TODO: 12/15/18 look at

    SpellParam targetParam;
    SpellParam numParam;
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

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.caster.world.isRemote || !(context.caster instanceof EntityPlayerMP)) return null;

        //Copypasteroo from PieceTrickDebug
        Double numberVal = this.<Double>getParamValue(context, numParam);
        Object targetVal = getParamValue(context, targetParam);

        String s = "null";
        if (targetVal != null)
            s = targetVal.toString();

        if (numberVal != null) {
            String numStr = "" + numberVal;
            if (numberVal - numberVal.intValue() == 0) {
                int numInt = numberVal.intValue();
                numStr = "" + numInt;
            }

            s = TextFormatting.AQUA + "[" + numStr + "] " + TextFormatting.RESET + s;
        }

        //End pasta

        TextComponentString component = new TextComponentString(s);
        MessageSpamlessChat message = new MessageSpamlessChat(component);
        RPSPacketHandler.NET.sendTo(message, (EntityPlayerMP) context.caster);

        return null;
    }
}
