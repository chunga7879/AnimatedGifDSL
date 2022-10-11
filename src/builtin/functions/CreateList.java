package builtin.functions;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates an empty list
 */
public class CreateList extends AbstractFunction {
    public final static String ACTUAL_NAME = "Create-List";

    @Override
    public Array call(Scope scope) {
        return new Array();
    }

    @Override
    public Array checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return checkReturn();
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>();
    }

    @Override
    public Array checkReturn() {
        return new Array();
    }
}
