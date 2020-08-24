package xyz.kamefrede.rpsideas.spells.selector;

import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import xyz.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceSelector;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class PieceSelectorTransmission extends PieceSelector {

    private SpellParam channel;

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

        SpellHelpers.ensurePositiveOrZero(this, channel);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double channelVal = SpellHelpers.getNumber(this, context, channel, 0);
        int channel = (int) channelVal;

        String key = "rpsideas:" + channel;

        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);

        if (data.getCustomData().hasKey(key)) {
            return data.getCustomData().getDouble(key);
        }

        throw new SpellRuntimeException(SpellRuntimeExceptions.NO_MESSAGE);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
