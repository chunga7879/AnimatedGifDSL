package builtin.functions.colour;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.IntegerValue;
import core.values.Value;

public class GetR extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        Colour colour = scope.getVar("$target").asColour();
        return new IntegerValue(colour.getR());
    }
}
