package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.IntegerValue;
import core.values.Value;
import utils.RandomNumber;

/**
 * Generate a random number between min and max (inclusive)
 */
public class Random extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        int min = scope.getVar("min").asInteger().get();
        int max = scope.getVar("max").asInteger().get();
        int num = RandomNumber.getRandomNumber(min, max);
        return new IntegerValue(num);
    }
}
