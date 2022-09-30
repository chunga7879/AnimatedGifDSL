package core.values;

import core.Scope;

public abstract class AbstractFunction extends Value {
    protected static final String NAME = "function";

    public AbstractFunction() {
        super(AbstractFunction.NAME);
    }

    public abstract Value call(Scope scope);

    @Override
    public AbstractFunction asFunction() {
        return this;
    }
}
