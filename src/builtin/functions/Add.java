package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Array;
import core.values.Null;
import core.values.Value;

public class Add extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        Value i = scope.getVar("item");
        Array a = scope.getVar("array").asArray();

        a.add(i);

        return new Null();
    }
}