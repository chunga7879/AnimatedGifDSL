package core.expressions.arithmetic;

import core.Scope;
import core.exceptions.NotComparable;
import core.expressions.Expression;
import core.values.IntegerValue;
import core.values.StringValue;
import core.values.Value;

public abstract class ArithmeticVisitor {
    private final String op;

    protected ArithmeticVisitor(String op) {
        this.op = op;
    }

    public Value visit(IntegerValue a, Expression b, Scope s) {
        throw new NotComparable(a, this.op);
    }


    public Value visit(StringValue a, Expression b, Scope s) {
        throw new NotComparable(a, this.op);
    }
}
