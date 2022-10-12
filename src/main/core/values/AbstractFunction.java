package core.values;

import core.Scope;
import core.exceptions.TypeError;
import core.expressions.ExpressionVisitor;

import java.util.Map;

public abstract class AbstractFunction extends Value {
    public static final String NAME = "function";
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

    public abstract Value checkArgs(Scope scope);

    public abstract Value checkReturn();

    public abstract Map<String, String> getParams();
}
