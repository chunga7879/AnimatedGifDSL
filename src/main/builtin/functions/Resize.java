package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;

import java.util.HashMap;
import java.util.Map;

public class Resize extends AbstractFunction {
    public final static String ACTUAL_NAME = "Resize";
    @Override
    public Image call(Scope scope) {
        ImmutableImage immutableImg = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asImage().get();
        int width = scope.getLocalVar("width").asInteger().get();
        int height = scope.getLocalVar("height").asInteger().get();

        // it scales an image up or down. This operation will change both the canvas and the image.
        return new Image(immutableImg.scaleTo(width, height));
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
            put("width", IntegerValue.NAME);
            put("height", IntegerValue.NAME);
        }};
    }

    @Override
    public Image checkReturn() {
        return new Image(null);
    }
}
