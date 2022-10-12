package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

public class GetWidth extends AbstractFunction {
    public final static String ACTUAL_NAME = "Get-Width";
    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar(AbstractFunction.PARAM_TARGET).asImage().get();

        return new IntegerValue(immutableImg.width);
    }

    @Override
    public IntegerValue checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return new IntegerValue(0);
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Image.NAME);
        }};
    }

    @Override
    public IntegerValue checkReturn() {
        return new IntegerValue(0);
    }
}
