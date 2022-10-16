package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.angles.Degrees;
import com.sksamuel.scrimage.angles.Radians;
import core.Scope;
import core.checkers.ArgumentChecker;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.Map;

import static com.sksamuel.scrimage.ImmutableImage.wrapAwt;

public class Rotate extends AbstractFunction {
    public final static String ACTUAL_NAME = "Rotate";
    @Override
    public Image call(Scope scope) {
        ImmutableImage immutableImg = scope.getLocalVar(AbstractFunction.PARAM_TARGET).asImage().get();
        int angle = scope.getLocalVar("angle").asInteger().get();

        return new Image(rotate(immutableImg, angle));
    }

    @Override
    public Image checkArgs(Scope scope) {
        ArgumentChecker.check(scope, getParams(), ACTUAL_NAME);
        return checkReturn();
    }

    @Override
    public Map<String, String> getParams() {
        return new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, Image.NAME);
            put("angle", IntegerValue.NAME);
        }};
    }

    @Override
    public Image checkReturn() {
        return new Image(null);
    }

    public ImmutableImage rotate(ImmutableImage image, int degree) {
         BufferedImage img = image.getType() != 0 ? new BufferedImage(image.width, image.height, image.getType()) : new BufferedImage(image.width, image.height, 2);
            Radians angle = (new Degrees(degree)).toRadians();
            Graphics2D g2 = (Graphics2D)img.getGraphics();

            g2.rotate(angle.value, img.getWidth() / 2, img.getHeight() / 2);
            g2.drawImage(image.awt(), 0, 0, (ImageObserver)null);
            g2.dispose();

            return wrapAwt(img, image.getMetadata());
    }
}
