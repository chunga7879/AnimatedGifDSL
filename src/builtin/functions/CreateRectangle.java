package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.Colors;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.expressions.ExpressionVisitor;
import core.values.*;
import core.values.Image;

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
    public void checkArgs(Scope scope) {
        Map<String, String> params = new HashMap<>() {{
            put("width", IntegerValue.NAME);
            put("height", IntegerValue.NAME);
            put("colour", Colour.NAME);
        }};
        ArgumentChecker.check(scope, params, ACTUAL_NAME);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
