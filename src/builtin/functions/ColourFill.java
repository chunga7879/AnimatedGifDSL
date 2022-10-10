package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.RGBColor;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import core.values.*;

import java.util.HashMap;
import java.util.Map;

public class ColourFill extends AbstractFunction {
    public final static String ACTUAL_NAME = "Colour-Fill";
    @Override
    public Value call(Scope scope) {
        ImmutableImage image = scope.getVar("$target").asImage().get();
        Colour colour = scope.getVar("colour").asColour();
        int r = colour.getR();
        int g = colour.getG();
        int b = colour.getB();

        ImmutableImage transparentImage = image.map((p) -> {
            return new RGBColor(r, g, b, p.alpha()).awt();
        });

        return new Image(transparentImage);
    }

    @Override
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("$target", Image.NAME);
            put("colour", Colour.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }
}
