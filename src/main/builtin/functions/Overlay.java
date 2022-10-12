package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;

import java.util.HashMap;
import java.util.Map;

public class Overlay extends AbstractFunction {
    public final static String ACTUAL_NAME = "Overlay";
    @Override
    public Image call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar(AbstractFunction.PARAM_TARGET).asImage().get();
        ImmutableImage on = scope.getVar(AbstractFunction.PARAM_ON).asImage().get();
        int x = scope.getVar("x").asInteger().get();
        int y = scope.getVar("y").asInteger().get();

        // (x, y): (0, 0) at the top left => Let me know If it needs to be top bottom (0, 0)
        return new Image(on.overlay(immutableImg, x, y));
    }

    @Override
    public Image checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return checkReturn();
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Image.NAME);
            put(AbstractFunction.PARAM_ON, Image.NAME);
            put("x", IntegerValue.NAME);
            put("y", IntegerValue.NAME);
        }};
    }

    @Override
    public Image checkReturn() {
        return new Image(null);
    }
}
