package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.RGBColor;
import core.Scope;
import core.expressions.ExpressionVisitor;
import core.values.*;

public class SetOpacity extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        ImmutableImage image = scope.getVar("$target").asImage().get();
        IntegerValue opacityAmount = scope.getVar("amount").asInteger();

        if (opacityAmount.get() > 100 || opacityAmount.get() < 0) {
            throw new IllegalArgumentException("Opacity value should be within [0, 100]");
        }

        // scrimage library accepts alpha range from 0 - 255
        int scaledOpacity = (int) (opacityAmount.get() * 2.55);

        ImmutableImage transparentImage = image.map((p) -> {
                if (p.alpha() != 0) {
                    return new RGBColor(p.red(), p.green(), p.blue(), scaledOpacity).awt();
                }
                return p.toColor().awt();
        });

        return new Image(transparentImage);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
