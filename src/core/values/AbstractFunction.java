package core.values;

import core.Scope;
import core.exceptions.TypeError;

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
}
