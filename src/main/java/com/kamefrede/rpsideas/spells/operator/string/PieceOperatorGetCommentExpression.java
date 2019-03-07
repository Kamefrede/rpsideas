package com.kamefrede.rpsideas.spells.operator.string;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.udojava.evalex.Expression;
import net.minecraft.util.text.TextFormatting;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceOperator;

import java.math.MathContext;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PieceOperatorGetCommentExpression extends PieceOperator {

    private static final Cache<String, Double> preEvaluated = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    public PieceOperatorGetCommentExpression(Spell spell) {
        super(spell);
    }

    private double parseExpression(String expression) throws Expression.ExpressionException, ExecutionException, UncheckedExecutionException {
        return preEvaluated.get(expression,
                () -> new ExpressionComment(expression, MathContext.DECIMAL128).eval().doubleValue());
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 2);

        if (this.comment == null || this.comment.isEmpty())
            throw new SpellCompilationException(SpellCompilationExceptions.NAN_COMMENT, x, y);
        try {
            parseExpression(this.comment);
        } catch (Expression.ExpressionException | ExecutionException | UncheckedExecutionException ex) {
            throw new SpellCompilationException(SpellCompilationExceptions.NAN_COMMENT, x, y);
        }
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (this.comment == null || this.comment.isEmpty())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);

        try {
            return parseExpression(this.comment);
        } catch (Expression.ExpressionException | ExecutionException | UncheckedExecutionException ex) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);
        }
    }

    @Override
    public void getTooltip(List<String> tooltip) {
        super.getTooltip(tooltip);
        if (this.comment != null && !this.comment.isEmpty()) {
            try {
                double result = parseExpression(this.comment);

                String resString = Double.toString(result);
                int index = resString.indexOf('.') + (result == (int) result ? 0 : 5);
                tooltip.set(0, tooltip.get(0) + TextFormatting.WHITE + " (" +
                        resString.substring(0, Math.min(index, resString.length())) + ")");
            } catch (Expression.ExpressionException | ExecutionException | UncheckedExecutionException ex) {
                // NO-OP
            }
        }
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
