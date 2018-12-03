package com.github.kamefrede.rpsideas.spells.selector;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorTransmission extends PieceSelector {

    SpellParam channel;

    public PieceSelectorTransmission(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(channel = new ParamNumber(SpellParams.GENERIC_NAME_CHANNEL, SpellParam.RED, true, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double channelVal = this.<Double>getParamEvaluation(channel);

        if(channelVal <= 0)
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_VALUE, x, y);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if(context.caster.world.isRemote) return null;
        Double channelVal = this.<Double>getParamValue(context, channel);
        if(channelVal == null) channelVal = 0D;
        int chanInt = channelVal.intValue();

        String key = "rpsideas:Entity" + context.caster.getEntityId() + "NumBroadcast";

        if(context.customData.containsKey(key)){

            Pair<Integer, Double> pair = (Pair<Integer, Double>) context.customData.get(key);
            return pair.getRight();
        }

        throw new SpellRuntimeException(SpellRuntimeExceptions.NO_MESSAGE);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
