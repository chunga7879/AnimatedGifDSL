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
        ImmutableImage immutableImg = scope.getVar(AbstractFunction.PARAM_TARGET).asImage().get();
        int angle = scope.getVar("angle").asInteger().get();

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
        if (image.getType() != 0) {
            return image.rotate(new Degrees(degree));
        } else {
            // as it throw error when calling image.rotate with type == 0, it will put image type 2 temporally
            BufferedImage img = new BufferedImage(image.width, image.height, 2);
            Radians angle = (new Degrees(degree)).toRadians();
            Graphics2D g2 = (Graphics2D)img.getGraphics();

            int offsetx;
            int offsety;
            if (angle.value < 0.0) {
                offsetx = 0;
                offsety = image.awt().getWidth();
            } else if (angle.value > 0.0) {
                offsetx = image.awt().getHeight();
                offsety = 0;
            } else {
                offsetx = 0;
                offsety = 0;
            }

            g2.translate(offsetx, offsety);
            g2.rotate(angle.value);
            g2.drawImage(image.awt(), 0, 0, (ImageObserver)null);
            g2.dispose();

            return wrapAwt(img, image.getMetadata());
        }
    }
}
