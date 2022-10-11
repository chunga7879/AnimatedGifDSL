package core.expressions.arithmetic;

import core.Scope;
import core.exceptions.InvalidOperation;
import core.expressions.Expression;
import core.values.IntegerValue;
import core.values.StringValue;
import core.values.Value;

public abstract class ArithmeticVisitor {
    private final String op;

    protected ArithmeticVisitor(String op) {
        this.op = op;
    }

    public Value visit(IntegerValue a, Value b, Scope s) {
        throw new InvalidOperation(a, this.op);
    }


    public Value visit(StringValue a, Value b, Scope s) {
        throw new InvalidOperation(a, this.op);
    }
}
