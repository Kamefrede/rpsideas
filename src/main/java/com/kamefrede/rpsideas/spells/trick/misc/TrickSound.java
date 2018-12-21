package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.ArrayList;
import java.util.List;

public class TrickSound extends PieceTrick {

    private SpellParam position;
    private SpellParam pitch;
    private SpellParam volume;
    private SpellParam instrument;

    public TrickSound(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(pitch = new ParamNumber(SpellParams.GENERIC_NAME_PITCH, SpellParam.GREEN, false, true));
        addParam(volume = new ParamNumber(SpellParams.GENERIC_NAME_VOLUME, SpellParam.YELLOW, false, false));
        addParam(instrument = new ParamNumber(SpellParams.GENERIC_NAME_INSTRUMENT, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        Double dVol = this.<Double>getParamEvaluation(volume);
        Double dPit = this.<Double>getParamEvaluation(pitch);
        Double dIn = this.<Double>getParamEvaluation(instrument);

        if (dPit != null && !(dPit >= 0 && dPit <= 24))
            throw new SpellCompilationException(SpellRuntimeExceptions.PITCH, x, y);

        if (dVol != null && !(dVol >= 0 && dVol <= 1))
            throw new SpellCompilationException(SpellRuntimeExceptions.VOLUME, x, y);

        if (dIn != null && !(dIn >= 0 && dIn <= 10))
            throw new SpellCompilationException(SpellRuntimeExceptions.INSTRUMENTS, x, y);

        meta.addStat(EnumSpellStat.POTENCY, 2);
        meta.addStat(EnumSpellStat.COST, 20);

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {

        List<SoundEvent> list = new ArrayList<SoundEvent>();
        list.add(SoundEvents.BLOCK_NOTE_HARP);
        list.add(SoundEvents.BLOCK_NOTE_BASEDRUM);
        list.add(SoundEvents.BLOCK_NOTE_SNARE);
        list.add(SoundEvents.BLOCK_NOTE_HAT);
        list.add(SoundEvents.BLOCK_NOTE_BASS);
        list.add(SoundEvents.BLOCK_NOTE_FLUTE);
        list.add(SoundEvents.BLOCK_NOTE_BELL);
        list.add(SoundEvents.BLOCK_NOTE_GUITAR);
        list.add(SoundEvents.BLOCK_NOTE_CHIME);
        list.add(SoundEvents.BLOCK_NOTE_XYLOPHONE);
        list.add(SoundEvents.BLOCK_NOTE_PLING);

        Vector3 posVal = this.getParamValue(context, position);
        Double instrumentVal = this.<Double>getParamValue(context, instrument);
        Double volVal = this.<Double>getParamValue(context, volume);
        Double pitchVal = this.<Double>getParamValue(context, pitch);
        int instrVal = instrumentVal.intValue();


        if (context.caster.world.isRemote) return null;

        if (posVal == null || posVal.isZero())
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        if (!context.isInRadius(posVal))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);

        BlockPos pos = new BlockPos(posVal.x, posVal.y, posVal.z);
        float f = (float) Math.pow(2.0D, (pitchVal - 12) / 12.0D);
        context.caster.world.playSound(null, pos, list.get(instrVal), SoundCategory.RECORDS, volVal.floatValue(), f);
        return true;
    }


}
