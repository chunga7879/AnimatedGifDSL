package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Function;
import core.values.Null;
import core.values.Value;

public class Print extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        System.out.println("builtin print: " + scope.getVar("msg").asString().get());
        return Null.NULL;
    }
}
