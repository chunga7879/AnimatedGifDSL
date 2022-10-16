package builtin.functions.colour;

import core.Scope;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.IntegerValue;

import java.util.HashMap;
import java.util.Map;

public class GetR extends AbstractFunction {
    public final static String ACTUAL_NAME = "Get-R";

    @Override
    public IntegerValue call(Scope scope) {
        Colour colour = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asColour();
        return new IntegerValue(colour.getR());
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Colour.NAME);
        }};
    }

    @Override
    public IntegerValue checkReturn() {
        return new IntegerValue(0);
    }
}
