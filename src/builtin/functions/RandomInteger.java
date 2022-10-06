package builtin.functions;

import core.Scope;
import core.values.AbstractFunction;
import core.values.IntegerValue;
import core.values.Value;

import java.util.Random;

/**
 * Generate a random number between min and max (inclusive)
 */
public class RandomInteger extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        int min = scope.getVar("min").asInteger().get();
        int max = scope.getVar("max").asInteger().get();

        Random random = new Random();
        int val = random.nextInt((max - min) + 1) + min;

        return new IntegerValue(val);
    }
}
