package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.RGBColor;
import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.*;

public class ColourFill extends AbstractFunction {
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
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
