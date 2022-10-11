package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.Image;
import core.values.*;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CreateRectangle extends AbstractFunction {
    public final static String ACTUAL_NAME = "Create-Rectangle";
    @Override
    public Value call(Scope scope) {
        int width = scope.getVar("width").asInteger().get();
        int height = scope.getVar("height").asInteger().get();
        Colour colour = scope.getVar("colour").asColour();

        ImmutableImage rectangle = ImmutableImage.filled(width, height, new Color(colour.getR(), colour.getG(), colour.getB()));

        return new Image(rectangle);
    }

    @Override
    public Image checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("width", IntegerValue.NAME);
            put("height", IntegerValue.NAME);
            put("colour", Colour.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
        return new Image(null);
    }
}
