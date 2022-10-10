package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.Value;

public class Resize extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar("$target").asImage().get();
        int width = scope.getVar("width").asInteger().get();
        int height = scope.getVar("height").asInteger().get();

        // it scales an image up or down. This operation will change both the canvas and the image.
        return new Image(immutableImg.scaleTo(width, height));
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
