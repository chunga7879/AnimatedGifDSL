package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.Value;

public class Overlay extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar("$target").asImage().get();
        ImmutableImage on = scope.getVar("on").asImage().get();
        int x = scope.getVar("x").asInteger().get();
        int y = scope.getVar("y").asInteger().get();

        // (x, y): (0, 0) at the top left => Let me know If it needs to be top bottom (0, 0)
        return new Image(on.overlay(immutableImg, x, y));
    }
}
