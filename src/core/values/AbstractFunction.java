package core.values;

import core.Scope;

public abstract class AbstractFunction extends Value {
    protected static final String NAME = "function";
    public static final String PARAM_TARGET = "$target";

    public AbstractFunction() {
        super(AbstractFunction.NAME);
    }

    public abstract Value call(Scope scope);

    @Override
    public AbstractFunction asFunction() {
        return this;
    }
}
