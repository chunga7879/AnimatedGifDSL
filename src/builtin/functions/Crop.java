package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.Value;

public class Crop extends AbstractFunction {

    @Override
    public Value call(Scope scope) {

        ImmutableImage immutableImg = scope.getVar("$target").asImage().get();
        int width = scope.getVar("width").asInteger().get();
        int height = scope.getVar("height").asInteger().get();

        return new Image(immutableImg.resizeTo(width, height));
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
