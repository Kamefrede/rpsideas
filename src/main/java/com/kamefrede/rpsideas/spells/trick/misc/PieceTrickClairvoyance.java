package com.kamefrede.rpsideas.spells.trick.misc;

import com.kamefrede.rpsideas.spells.base.SpellParams;
import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickClairvoyance extends PieceTrick {

    private SpellParam time;
    private SpellParam ray;
    private SpellParam distance;

    public PieceTrickClairvoyance(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.GREEN, false, true));
        addParam(ray = new ParamVector(SpellParams.GENERIC_VAZKII_RAY, SpellParam.YELLOW, false, false));
        addParam(distance = new ParamNumber(SpellParam.GENERIC_NAME_DISTANCE, SpellParam.BLUE, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
        super.addToMetadata(meta);
        double voyanceTime = SpellHelpers.ensurePositiveAndNonzero(this, time, 0);
        double voyanceDist = SpellHelpers.ensurePositiveAndNonzero(this, distance, 0);
        meta.addStat(EnumSpellStat.COST, (int) (Math.pow(voyanceDist, 3) * ((voyanceTime / 20) * 150)));
        meta.addStat(EnumSpellStat.POTENCY, (int) (Math.pow(voyanceDist, 2) * ((voyanceTime / 20) * 15)));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return super.execute(context);
    }
}
