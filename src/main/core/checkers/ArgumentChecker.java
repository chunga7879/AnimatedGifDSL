package core.checkers;

import core.Scope;
import core.exceptions.FunctionException;
import core.values.Unknown;

import java.util.Map;
import java.util.Objects;

public class ArgumentChecker {
    public static void check(Scope scope, Map<String, String> params, String functionName) {
        if (scope.getLocalSize() != params.size()) {
            throw new FunctionException("Invalid number of arguments provided to function \"" + functionName + "\"; requires (" +
                String.join(", ", params.keySet()) + "), got " + scope.getLocalKeyString());
        }
        for (Map.Entry<String, String> param : params.entrySet()) {
            String paramName = param.getKey();
            if (!scope.hasVarLocal(paramName)) {
                throw new FunctionException("Argument \"" + paramName + "\" not provided to function \"" + functionName + "\"");
            }
            String expectedType = param.getValue();
            String actualType = scope.getLocalVar(paramName).getTypeName();
            if (!(Objects.equals(expectedType, actualType)
                || Objects.equals(Unknown.NAME, expectedType)
                || Objects.equals(Unknown.NAME, actualType))) {
                throw new FunctionException("Argument " + paramName + " is of type " + actualType + " but expected type "
                    + expectedType + " for function \"" + functionName + "\"");
            }
        }
    }
}
