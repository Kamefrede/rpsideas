package com.kamefrede.rpsideas.spells.selector;

import com.kamefrede.rpsideas.util.helpers.SpellHelpers;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;

public class PieceMacroDefaultedVector extends SpellPiece {

    private SpellParam number;
    private SpellParam x;
    private SpellParam y;
    private SpellParam z;

    public PieceMacroDefaultedVector(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.YELLOW, true, false));
        addParam(x = new ParamNumber(SpellParam.GENERIC_NAME_X, SpellParam.RED, true, false));
        addParam(y = new ParamNumber(SpellParam.GENERIC_NAME_Y, SpellParam.GREEN, true, false));
        addParam(z = new ParamNumber(SpellParam.GENERIC_NAME_Z, SpellParam.BLUE, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        double num = SpellHelpers.getNumber(this, context, number, 0.5);
        double xVal = SpellHelpers.getNumber(this, context, x, num);
        double yVal = SpellHelpers.getNumber(this, context, y, num);
        double zVal = SpellHelpers.getNumber(this, context, z, num);
        return new Vector3(xVal, yVal, zVal);
    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.SELECTOR;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }

    @Override
    public Object evaluate() throws SpellCompilationException {
        return null;
    }
}
