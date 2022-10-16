package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.RGBColor;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.Image;

import java.util.HashMap;
import java.util.Map;

public class ColourFill extends AbstractFunction {
    public final static String ACTUAL_NAME = "Colour-Fill";
    @Override
    public Image call(Scope scope) {
        ImmutableImage image = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asImage().get();
        Colour colour = scope.getLocalVar("colour").asColour();
        int r = colour.getR();
        int g = colour.getG();
        int b = colour.getB();

        ImmutableImage transparentImage = image.map((p) -> {
            return new RGBColor(r, g, b, p.alpha()).awt();
        });

        return new Image(transparentImage);
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
            put("colour", Colour.NAME);
        }};
    }

    @Override
    public Image checkReturn() {
        return new Image(null);
    }
}
