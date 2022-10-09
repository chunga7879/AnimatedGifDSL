package core.expressions.comparison;

import core.Scope;
import core.exceptions.InvalidOperation;
import core.expressions.Expression;
import core.values.BooleanValue;
import core.values.IntegerValue;
import core.values.StringValue;
import core.values.Value;

public abstract class ComparisonVisitor {
    private final String op;

    protected ComparisonVisitor(String op) {
        this.op = op;
    }

    public BooleanValue visit(IntegerValue a, Expression b, Scope s) {
        throw new InvalidOperation(a, this.op);
    }

    public BooleanValue visit(StringValue a, Expression b, Scope s) {
        throw new InvalidOperation(a, this.op);
    }
}
