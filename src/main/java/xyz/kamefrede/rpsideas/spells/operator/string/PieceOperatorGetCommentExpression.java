package com.kamefrede.rpsideas.spells.operator.string;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.kamefrede.rpsideas.spells.base.SpellCompilationExceptions;
import com.kamefrede.rpsideas.spells.base.SpellRuntimeExceptions;
import com.udojava.evalex.Expression;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import vazkii.arl.util.RenderHelper;
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
    public void drawCommentText(int tooltipX, int tooltipY, List<String> commentText) {
        super.drawCommentText(tooltipX, tooltipY, commentText);
        if (this.comment != null && !this.comment.isEmpty()) {
            int maxSize = 0;
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            for (String line : commentText) {
                int size = fontRenderer.getStringWidth(line);
                if (size > maxSize)
                    maxSize = size;
            }

            int xEnd = tooltipX + 8 + maxSize;
            int yMiddle = tooltipY - 14 - commentText.size() * 5;

            try {
                double result = parseExpression(this.comment);

                String resString = Double.toString(result);
                int index = resString.indexOf('.') + (result == (int) result ? 0 : 5);
                RenderHelper.renderTooltip(xEnd, yMiddle, Lists.newArrayList("= " +
                        resString.substring(0, Math.min(index, resString.length()))),
                        0x5000a0a0, 0xf0001e1e);
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
