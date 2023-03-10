package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
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

        ImmutableImage immutableImg = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asImage().get();
        int width = scope.getLocalVar("width").asInteger().get();
        int height = scope.getLocalVar("height").asInteger().get();

        return new Image(immutableImg.resizeTo(width, height));
    }

    @Override
    public String getFunctionName() {
        return ACTUAL_NAME;
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
