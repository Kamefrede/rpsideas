package com.kamefrede.rpsideas.spells.trick.misc;

import com.google.common.collect.ImmutableList;
import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.List;

public class TrickSound extends PieceTrick {


    private static final List<SoundEvent> SOUND_EVENTS = ImmutableList.of(
            SoundEvents.BLOCK_NOTE_HARP,
            SoundEvents.BLOCK_NOTE_BASEDRUM,
            SoundEvents.BLOCK_NOTE_SNARE,
            SoundEvents.BLOCK_NOTE_HAT,
            SoundEvents.BLOCK_NOTE_BASS,
            SoundEvents.BLOCK_NOTE_FLUTE,
            SoundEvents.BLOCK_NOTE_BELL,
            SoundEvents.BLOCK_NOTE_GUITAR,
            SoundEvents.BLOCK_NOTE_CHIME,
            SoundEvents.BLOCK_NOTE_XYLOPHONE,
            SoundEvents.BLOCK_NOTE_PLING);

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
        addParam(instrument = new ParamNumber(SpellParams.GENERIC_NAME_INSTRUMENT, SpellParam.RED, false, false));
        addParam(pitch = new ParamNumber(SpellParams.GENERIC_NAME_PITCH, SpellParam.GREEN, true, false));
        addParam(volume = new ParamNumber(SpellParams.GENERIC_NAME_VOLUME, SpellParam.YELLOW, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        double dVol = SpellHelpers.ensurePositiveOrZero(this, volume);
        double dPit = SpellHelpers.ensurePositiveOrZero(this, pitch);
        double dIn = SpellHelpers.ensurePositiveOrZero(this, instrument);

        if (dPit < 0 || dPit > 24)
            throw new SpellCompilationException(SpellCompilationExceptions.PITCH, x, y);

        if (dVol < 0 || dVol > 1)
            throw new SpellCompilationException(SpellCompilationExceptions.VOLUME, x, y);

        if (dIn < 0 || dIn > 10)
            throw new SpellCompilationException(SpellCompilationExceptions.INSTRUMENTS, x, y);

        meta.addStat(EnumSpellStat.POTENCY, 4);

    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {


        if (context.caster.world.isRemote) return null;

        BlockPos pos = SpellHelpers.getBlockPos(this, context, position);
        double instrumentVal = this.getParamValue(context, instrument);
        double volVal = SpellHelpers.getNumber(this, context, volume, 0);
        double pitchVal = SpellHelpers.getNumber(this, context, pitch, 0);

        float f = (float) Math.pow(2, (pitchVal - 12) / 12.0);
        context.caster.world.playSound(null, pos, SOUND_EVENTS.get((int) instrumentVal), SoundCategory.RECORDS, (float) volVal, f);
        return true;
    }


}
