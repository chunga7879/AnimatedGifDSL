package builtin.functions;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.exceptions.FunctionException;
import core.expressions.ExpressionVisitor;
import core.values.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Add extends AbstractFunction {
    public final static String ACTUAL_NAME = "Add";

    @Override
    public Value call(Scope scope) {
        Value i = scope.getVar("item");
        Array a = scope.getVar("array").asArray();

        a.add(i);

        return Null.NULL;
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("array", Array.NAME);
        }};
        if (scope.getSize() != 2) {
            throw new FunctionException("invalid number of arguments provided to function call: " + ACTUAL_NAME);
        }
        if (!scope.hasVar("item")) {
            throw new FunctionException("argument item not provided to function call: " + ACTUAL_NAME);
        }
        if (!scope.hasVar("array")) {
            throw new FunctionException("argument array not provided to function call: " + ACTUAL_NAME);
        }
        String actualType = scope.getVar("array").getTypeName();
        if (!Objects.equals(Array.NAME, actualType)) {
            throw new FunctionException("argument array is of type " + actualType + " but expected " + Array.NAME
                + " function call: " + ACTUAL_NAME);
        }
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
