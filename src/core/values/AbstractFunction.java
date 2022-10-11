package core.values;

import core.Scope;
import core.exceptions.TypeError;
import core.expressions.ExpressionVisitor;
import core.expressions.Expression;

import java.util.Map;
import java.util.Objects;

public abstract class AbstractFunction extends Value {
    protected static final String NAME = "function";
    public static final String PARAM_TARGET = "$target";
    public static final String PARAM_ON = "$on";

    public AbstractFunction() {
        super(AbstractFunction.NAME);
    }

    public Value call(Scope scope) {
        throw new TypeError("this function requires a target");
    }

    @Override
    public AbstractFunction asFunction() {
        return this;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public abstract void checkArgs(Scope scope);
}
