package com.kamefrede.rpsideas.spells.trick.block;

import com.kamefrede.rpsideas.network.MessageAddSilencedBlock;
import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSilence extends PieceTrick {

    private SpellParam volume;
    private SpellParam position;
    private SpellParam time;
    private SpellParam radius;

    public PieceTrickSilence(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(volume = new ParamNumber(SpellParams.GENERIC_NAME_VOLUME, SpellParam.RED, true, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.BLUE, true, true));
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.GREEN, false, false));
        addParam(radius = new ParamNumber(SpellParam.GENERIC_NAME_RADIUS, SpellParam.YELLOW, true, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double vol = SpellHelpers.ensurePositiveOrZero(this, volume);
        double tim = SpellHelpers.ensurePositiveAndNonzero(this, time);
        double rad = SpellHelpers.ensurePositiveAndNonzero(this, radius);
        if (vol < 0 || vol > 1)
            throw new SpellCompilationException(SpellCompilationExceptions.VOLUME, x, y);
        if (rad > 16 || rad < 0)
            throw new SpellCompilationException(SpellCompilationExceptions.RADIUS, x, y);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double vol = SpellHelpers.getNumber(this, context, volume, 0);
        double tim = Math.min(SpellHelpers.getNumber(this, context, time, 400), 4800);
        double rad = Math.max(SpellHelpers.getNumber(this, context, radius, 1), SpellContext.MAX_DISTANCE);
        Vector3 pos = SpellHelpers.getVector3(this, context, position, true, false);

        if (!context.caster.world.isRemote)
            PacketHandler.NETWORK.sendToDimension(new MessageAddSilencedBlock((int) tim, context.caster.world.getWorldTime(), pos.toBlockPos(), (int) rad, (float) vol, context.caster.world.provider.getDimension()), context.caster.world.provider.getDimension());


        return null;
    }
}
