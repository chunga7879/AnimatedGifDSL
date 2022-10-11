package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.Colors;
import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Colour;
import core.values.Image;
import core.values.Value;

import java.awt.*;

public class CreateRectangle extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        int width = scope.getVar("width").asInteger().get();
        int height = scope.getVar("height").asInteger().get();
        Colour colour = scope.getVar("colour").asColour();

        ImmutableImage rectangle = ImmutableImage.filled(width, height, new Color(colour.getR(), colour.getG(), colour.getB()));

        return new Image(rectangle);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
