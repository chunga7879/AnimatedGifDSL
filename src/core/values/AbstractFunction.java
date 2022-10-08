package core.values;

import core.Scope;
import core.exceptions.TypeError;

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
}
