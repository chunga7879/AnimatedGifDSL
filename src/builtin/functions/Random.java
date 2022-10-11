package builtin.functions;

import core.Scope;
import core.checkers.ArgumentChecker;
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
        int min = scope.getVar("min").asInteger().get();
        int max = scope.getVar("max").asInteger().get();
        int num = RandomNumber.getRandomNumber(min, max);
        return new IntegerValue(num);
    }

    @Override
    public IntegerValue checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("min", IntegerValue.NAME);
            put("max", IntegerValue.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
        return new IntegerValue(0);
    }
}
