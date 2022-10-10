package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.angles.Degrees;
import com.sksamuel.scrimage.angles.Radians;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.Value;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static com.sksamuel.scrimage.ImmutableImage.wrapAwt;

public class Rotate extends AbstractFunction {
    @Override
    public Value call(Scope scope) {
        ImmutableImage immutableImg = scope.getVar("$target").asImage().get();
        int angle = scope.getVar("angle").asInteger().get();

        return new Image(rotate(immutableImg, angle));
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
