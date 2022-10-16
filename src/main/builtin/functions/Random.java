package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.IntegerValue;
import utils.RandomNumber;

import java.util.HashMap;
import java.util.Map;

/**
 * Generate a random number between min and max (inclusive)
 */
public class Random extends AbstractFunction {
    public final static String ACTUAL_NAME = "Random";

    @Override
    public IntegerValue call(Scope scope) {
        int min = scope.getLocalVar("min").asInteger().get();
        int max = scope.getLocalVar("max").asInteger().get();
        int num = RandomNumber.getRandomNumber(min, max);
        return new IntegerValue(num);
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put("min", IntegerValue.NAME);
            put("max", IntegerValue.NAME);
        }};
    }

    @Override
    public IntegerValue checkReturn() {
        return new IntegerValue(0);
    }
}
