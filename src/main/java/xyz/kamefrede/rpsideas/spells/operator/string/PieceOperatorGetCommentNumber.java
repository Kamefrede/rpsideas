package xyz.kamefrede.rpsideas.spells.operator.string;

import xyz.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import xyz.kamefrede.rpsideas.spells.base.SpellParams;
import xyz.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;

public class PieceOperatorGetCommentNumber extends PieceOperator {

    private SpellParam base;

    public static double parseDouble(String s, int radix) throws NumberFormatException {
        if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
            radix = 10;

        if (radix == 10)
            return Double.parseDouble(s);

        int idx = s.indexOf('.');
        if (idx == -1)
            return Integer.parseInt(s, radix);

        String before = s.substring(0, idx);
        String after = s.substring(idx);

        double power = Math.pow(radix, -after.length());
        double value = 0;

        for (int i = 0; i < after.length() + before.length(); i++) {
            char c = i < after.length() ?
                    after.charAt(after.length() - 1 - i) :
                    before.charAt(before.length() - after.length() - i);

            int charValue = Character.getNumericValue(c);
            if (charValue < 1)
                throw new NumberFormatException("Invalid symbol " + c + " @ " + s);

            value += charValue * power;
            power *= radix;
        }

        return value;
    }

    public PieceOperatorGetCommentNumber(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(base = new ParamNumber(SpellParams.GENERIC_NAME_RADIX, SpellParam.BLUE, true, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        if (this.comment == null || this.comment.isEmpty())
            throw new SpellCompilationException(SpellCompilationExceptions.NAN_COMMENT, x, y);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (this.comment == null || this.comment.isEmpty())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);

        Double radixVal = getParamValue(context, base);
        int radix = radixVal == null ? 10 : radixVal.intValue();

        String comment = this.comment;

        if (comment.startsWith("0b") && (radixVal == null || radix == 2)) {
            radix = 2;
            comment = comment.substring(2);
        } else if (this.comment.startsWith("0x") && (radixVal == null || radix == 16)) {
            radix = 16;
            comment = comment.substring(2);
        } else if (this.comment.startsWith("0o") && (radixVal == null || radix == 8)) {
            radix = 8;
            comment = comment.substring(2);
        } else if (this.comment.startsWith("0") && (radixVal == null || radix == 8))
            radix = 8;

        try {
            return parseDouble(comment, radix);
        } catch (NumberFormatException ex) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);
        }
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
