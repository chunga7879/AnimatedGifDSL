package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.Colors;
import core.Scope;
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
        System.out.println(colour.getR());
        System.out.println(colour.getG());
        System.out.println(colour.getB());

        ImmutableImage rectangle = ImmutableImage.filled(width, height, new Color(colour.getR(), colour.getG(), colour.getB()));

        return new Image(rectangle);
    }
}
