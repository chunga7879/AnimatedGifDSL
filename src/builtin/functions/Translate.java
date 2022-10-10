package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.Colors;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.Value;

public class Translate extends AbstractFunction {

    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar("$target").asImage().get();
        int x = scope.getVar("x").asInteger().get();
        int y = scope.getVar("y").asInteger().get();

        return new Image(translate(immutableImg, x, y));
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
