package builtin.functions.colour;

import core.Scope;
import core.expressions.Expression;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.IntegerValue;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateColour extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        IntegerValue r = scope.getVar("r").asInteger();
        IntegerValue g = scope.getVar("g").asInteger();
        IntegerValue b = scope.getVar("b").asInteger();

        return new Colour(r.get(), g.get(), b.get());
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
           put("r", IntegerValue.NAME);
           put("g", IntegerValue.NAME);
           put("b", IntegerValue.NAME);
        }};
        checker(scope, params);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
