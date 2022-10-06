package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.Null;
import core.values.Value;

/**
 * Creates an empty list and assigns it to a variable
 */
public class CreateList extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        // TODO: fix name parameter
        scope.setVar("array", new Array());
        return Null.NULL;
    }
}
