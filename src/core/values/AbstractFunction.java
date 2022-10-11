package core.values;

import core.Scope;
import core.exceptions.TypeError;
import core.expressions.ExpressionVisitor;

public abstract class AbstractFunction extends Value {
    protected static final String NAME = "function";

    public AbstractFunction() {
        super(AbstractFunction.NAME);
    }

    public Value call(Scope scope) {
        throw new TypeError("this function requires a target");
    }

    public Value call(Scope scope, Value target) {
        throw new TypeError("this function does not take a target");
    }

    @Override
    public AbstractFunction asFunction() {
        return this;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
