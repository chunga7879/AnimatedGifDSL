package builtin.functions.colour;

import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.*;

public class CreateColour extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        IntegerValue r = scope.getVar("r").asInteger();
        IntegerValue b = scope.getVar("b").asInteger();
        IntegerValue g = scope.getVar("g").asInteger();

        return new Colour(r.get(), g.get(), b.get());
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
