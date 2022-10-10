package core.checkers;

import core.Scope;
import core.exceptions.FunctionException;

import java.util.Map;
import java.util.Objects;

public class ArgumentChecker {
    public static void check(Scope scope, Map<String, String> params, String functionName) {
        if (scope.getSize() != params.size()) {
            throw new FunctionException("invalid number of arguments provided to function call: " + functionName);
        }
        for (Map.Entry<String, String> param : params.entrySet()) {
            String paramName = param.getKey();
            if (!scope.hasVar(paramName)) {
                throw new FunctionException("argument " + paramName + " not provided to function call: " + functionName);
            }
            String expectedType = param.getValue();
            String actualType = scope.getVar(paramName).getTypeName();
            if (!Objects.equals(expectedType, actualType)) {
                throw new FunctionException("argument " + paramName + " is of type " + actualType + " but expected "
                    + expectedType + " function call: " + functionName);
            }
        }
    }
}
