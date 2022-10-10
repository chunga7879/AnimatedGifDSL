package builtin.functions.colour;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.IntegerValue;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

public class GetR extends AbstractFunction {
    public final static String ACTUAL_NAME = "Get-R";

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
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
