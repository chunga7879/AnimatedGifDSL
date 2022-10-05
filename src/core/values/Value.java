package core.values;

import core.Scope;
import core.exceptions.InvalidArithmeticOperator;
import core.exceptions.NotComparable;
import core.exceptions.TypeError;
import core.expressions.arithmetic.ArithmeticVisitor;
import core.expressions.comparison.ComparisonVisitor;
import core.expressions.Expression;

public abstract class Value implements Expression {
    private final String typeName;

    public Value(String typeName) {
        this.typeName = typeName;
    }

    public StringValue asString() {
        throw new TypeError(this, StringValue.NAME);
    }

    public AbstractFunction asFunction() {
        throw new TypeError(this, Function.NAME);
    }

    public Array asArray() {
        throw new TypeError(this, Array.NAME);
    }

    public Colour asColour() {
        throw new TypeError(this, Colour.NAME);
    }

    public IntegerValue asInteger() {
        throw new TypeError(this, IntegerValue.NAME);
    }

    public BooleanValue asBoolean() {
        throw new TypeError(this, BooleanValue.NAME);
    }

    public BooleanValue acceptCV(ComparisonVisitor cv, Expression b, Scope s) {
        throw new NotComparable(this);
    }

    public Value acceptAV(ArithmeticVisitor av, Expression b, Scope s) {
        throw new InvalidArithmeticOperator(this);
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public Value evaluate(Scope s) {
        return this;
    }
}
