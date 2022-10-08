package builtin.functions;

import core.Scope;
import core.values.*;

public class CreateColour extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        IntegerValue r = scope.getVar("r").asInteger();
        IntegerValue g = scope.getVar("b").asInteger();
        IntegerValue b = scope.getVar("g").asInteger();

        return new Colour(r.get(), g.get(), b.get());
    }
}
