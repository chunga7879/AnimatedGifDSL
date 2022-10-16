package core.checkers;

import core.Scope;
import core.exceptions.FunctionException;
import core.values.Value;

import java.util.Map;

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
            Value argument = scope.getLocalVar(paramName);
            if (!TypeChecker.checkValueIsTypeOrUnknown(argument, expectedType)) {
                throw new FunctionException("Argument \"" + paramName + "\" is of type " + argument.getTypeName() + " but expected type "
                    + expectedType + " for function \"" + functionName + "\"");
            }
        }
    }
}
