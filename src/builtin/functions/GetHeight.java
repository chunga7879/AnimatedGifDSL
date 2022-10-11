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

public class GetHeight extends AbstractFunction {
    public final static String ACTUAL_NAME = "Get-Height";
    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar(AbstractFunction.PARAM_TARGET).asImage().get();

        return new IntegerValue(immutableImg.height);
    }

    @Override
    public IntegerValue checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Image.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
        return new IntegerValue(0);
    }
}
