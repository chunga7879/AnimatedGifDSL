package builtin.functions.colour;

import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.IntegerValue;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

public class GetR extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        Colour colour = scope.getVar("$target").asColour();
        return new IntegerValue(colour.getR());
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("$target", Colour.NAME);
        }};
        checker(scope, params);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
