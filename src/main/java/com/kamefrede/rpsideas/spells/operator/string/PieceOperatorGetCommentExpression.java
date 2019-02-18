package com.kamefrede.rpsideas.spells.operator.string;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.udojava.evalex.Expression;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceOperator;

import java.math.MathContext;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class PieceOperatorGetCommentExpression extends PieceOperator {

    private static final Cache<String, Double> preEvaluated = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .build();

    public PieceOperatorGetCommentExpression(Spell spell) {
        super(spell);
    }

    private double parseExpression(String expression) throws Expression.ExpressionException, ExecutionException {
        return preEvaluated.get(expression,
                () -> new ExpressionComment(expression, MathContext.DECIMAL128).eval().doubleValue());
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 2);

        if (this.comment.isEmpty())
            throw new SpellCompilationException(SpellCompilationExceptions.NAN_COMMENT, x, y);
        try {
            parseExpression(this.comment);
        } catch (Expression.ExpressionException | ExecutionException ex) {
            throw new SpellCompilationException(SpellCompilationExceptions.NAN_COMMENT, x, y);
        }
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (this.comment.isEmpty())
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);

        try {
            return parseExpression(this.comment);
        } catch (Expression.ExpressionException | ExecutionException ex) {
            throw new SpellRuntimeException(SpellRuntimeExceptions.NAN);
        }
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
