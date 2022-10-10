package core.values;

import core.Scope;
import core.exceptions.FunctionException;
import core.exceptions.TypeError;
import core.expressions.Expression;

import java.util.Map;
import java.util.Objects;

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

    public abstract void checkArgs(Scope scope);

    protected void checker(Scope scope, Map<String, String> params) {
        if (scope.getSize() != params.size()) {
            throw new FunctionException("Invalid number of arguments");
        }
        for (Map.Entry<String, String> param : params.entrySet()) {
            String paramName = param.getKey();
            if (!scope.hasVar(paramName)) {
                throw new FunctionException("argument " + paramName + " not provided");
            }
            String expectedType = param.getValue();
            String actualType = scope.getVar(paramName).getTypeName();
            if (!Objects.equals(expectedType, actualType)) {
                throw new FunctionException("argument " + paramName + " is of type " + actualType + " but expected " + expectedType);
            }
        }
    }
}
