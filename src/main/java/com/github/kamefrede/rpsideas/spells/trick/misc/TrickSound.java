package com.github.kamefrede.rpsideas.spells.trick.misc;

import com.github.kamefrede.rpsideas.spells.base.SpellParams;
import com.github.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class TrickSound extends PieceTrick {

    SpellParam position;
    SpellParam pitch;
    SpellParam volume;
    SpellParam instrument;

    public TrickSound(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(pitch = new ParamNumber(SpellParams.GENERIC_NAME_PITCH, SpellParam.GREEN, false, false));
        addParam(volume = new ParamNumber(SpellParams.GENERIC_NAME_VOLUME, SpellParam.YELLOW, false, false));
        addParam(instrument = new ParamNumber(SpellParams.GENERIC_NAME_INSTRUMENT, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double instrumentVal = this.<Double>getParamEvaluation(instrument);
        Integer instr = instrumentVal.intValue();
        Double volumeVal = this.<Double>getParamEvaluation(volume);
        if(volumeVal == null || !(0 <= volumeVal || volumeVal <= 1))
            throw new SpellCompilationException(SpellRuntimeExceptions.VOLUME, x, y);
        if(instr == null || !(0 <= instr || instr <= 9))
            throw new SpellCompilationException(SpellRuntimeExceptions.INSTRUMENTS, x, y);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 posVal = this.<Vector3>getParamValue(context, position);
        Double instrumentVal = this.<Double>getParamValue(context, instrument);
        Integer instrVal = instrumentVal.intValue();
        Double volVal = this.<Double>getParamValue(context, volume);
        Double pitchVal = this.<Double>getParamValue(context, pitch);

        if(pitchVal == null) pitchVal = 0D;
        if(context.caster.world.isRemote) return null;
        if(posVal == null || posVal.isZero())
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        if(!context.isInRadius(posVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        return super.execute(context);
    }
}
