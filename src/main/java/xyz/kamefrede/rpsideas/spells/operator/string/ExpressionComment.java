package xyz.kamefrede.rpsideas.spells.operator.string;

import xyz.kamefrede.rpsideas.util.RPSMethodHandles;
import xyz.kamefrede.rpsideas.util.helpers.Selector;
import com.udojava.evalex.Expression;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @author WireSegal
 * Created at 3:08 PM on 2/17/19.
 */
public class ExpressionComment extends Expression {

    public static final BigDecimal PHI = new BigDecimal(
            "1.6180339887498948482045868343656381177203091798057628621354486227052604628189024497072072041893911374");
    public static final BigDecimal TAU = new BigDecimal(
            "6.2831853071795864769252867665590057683943387987502116419498891846156328125724179972560696506842341359");

    public ExpressionComment(String expression) {
        this(expression, MathContext.DECIMAL32);
    }

    public ExpressionComment(String expression, MathContext defaultMathContext) {
        super(expression, defaultMathContext);

        addOperator("and", 2, false,
                (v1, v2) -> new BigDecimal(v1.toBigInteger()
                        .and(v2.toBigInteger())));
        addOperator("or", 2, false,
                (v1, v2) -> new BigDecimal(v1.toBigInteger()
                        .or(v2.toBigInteger())));
        addOperator("xor", 2, false,
                (v1, v2) -> new BigDecimal(v1.toBigInteger()
                        .xor(v2.toBigInteger())));
        addOperator("shl", 2, false,
                (v1, v2) -> new BigDecimal(v1.toBigInteger()
                        .shiftLeft(v2.intValue())));
        addOperator("shr", 2, false,
                (v1, v2) -> new BigDecimal(v1.toBigInteger()
                        .shiftRight(v2.intValue())));
        addFunction("bitnot", 1,
                it -> new BigDecimal(it.get(0).toBigInteger()
                        .not()));

        setVariable("\u03c0", PI); // Pi character
        setVariable("TAU", TAU);
        setVariable("\u03c4", TAU); // Tau character
        setVariable("PHI", PHI);
        setVariable("\u03c6", PHI); // Phi character

        RPSMethodHandles.getExpressionFunctions(this).remove("RANDOM");
    }

    protected void addOperator(String operator, int precedence, boolean leftAssoc, BinaryOperator<BigDecimal> impl) {
        addOperator(new FunctionalOperator(operator, precedence, leftAssoc, impl));
    }

    protected void addFunction(String name, int numParams, Selector<BigDecimal> impl) {
        addFunction(new FunctionalFunction(name, numParams, impl));
    }

    protected class FunctionalOperator extends Operator {
        private final BinaryOperator<BigDecimal> impl;

        public FunctionalOperator(String operator, int precedence, boolean leftAssoc, BinaryOperator<BigDecimal> impl) {
            super(operator, precedence, leftAssoc);
            this.impl = impl;
        }

        @Override
        public BigDecimal eval(BigDecimal v1, BigDecimal v2) {
            return impl.apply(v1, v2);
        }
    }

    protected class FunctionalFunction extends Function {
        private final Selector<BigDecimal> impl;

        public FunctionalFunction(String name, int numParams, Selector<BigDecimal> impl) {
            super(name, numParams);
            this.impl = impl;
        }

        @Override
        public BigDecimal eval(List<BigDecimal> parameters) {
            return impl.apply(parameters);
        }
    }
}
