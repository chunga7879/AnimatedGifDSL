package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.Colors;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;

import java.util.HashMap;
import java.util.Map;

public class Translate extends AbstractFunction {
    public final static String ACTUAL_NAME = "Translate";

    @Override
    public Image call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar(AbstractFunction.PARAM_TARGET).asImage().get();
        int x = scope.getVar("x").asInteger().get();
        int y = scope.getVar("y").asInteger().get();

        return new Image(translate(immutableImg, x, y));
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
            put("x", IntegerValue.NAME);
            put("y", IntegerValue.NAME);
        }};
    }

    @Override
    public Image checkReturn() {
        return new Image(null);
    }

    public  ImmutableImage translate(ImmutableImage image, int x, int y) {
        if (image.getType() != 0) {
            return image.translate(x, y);
        } else {
            ImmutableImage background = ImmutableImage.filled(image.width, image.height, Colors.Transparent.toAWT());
            return background.overlay(image, x, y);
        }
    }
}
