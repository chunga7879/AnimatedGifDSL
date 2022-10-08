package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.Value;

/**
 * Creates an empty list
 */
public class CreateList extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        return new Array();
    }
}
