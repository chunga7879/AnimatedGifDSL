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

public class Crop extends AbstractFunction {
    public final static String ACTUAL_NAME = "Crop";

    @Override
    public Value call(Scope scope) {

        ImmutableImage immutableImg = scope.getVar(AbstractFunction.PARAM_TARGET).asImage().get();
        int width = scope.getVar("width").asInteger().get();
        int height = scope.getVar("height").asInteger().get();

        return new Image(immutableImg.resizeTo(width, height));
    }

    @Override
    public Image checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Image.NAME);
            put("width", IntegerValue.NAME);
            put("height", IntegerValue.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
        return new Image(null);
    }
}
