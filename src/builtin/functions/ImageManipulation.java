package builtin.functions;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.angles.Degrees;
import com.sksamuel.scrimage.angles.Radians;
import com.sksamuel.scrimage.canvas.Canvas;
import com.sksamuel.scrimage.color.RGBColor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static com.sksamuel.scrimage.ImmutableImage.create;
import static com.sksamuel.scrimage.ImmutableImage.wrapAwt;

public class ImageManipulation {

    // resize with width, height
    public static ImmutableImage resize(ImmutableImage image, int width, int height) {
        // it scales an image up or down. This operation will change both the canvas and the image.
        return image.scaleTo(width, height);
    }

    // use previous one used in Scrimage Example
    public static ImmutableImage color(ImmutableImage image, int r, int g, int b) {
        return image.map((p) -> {
            if (p.alpha() != 0) {
                return new RGBColor(r, g, b, p.alpha()).awt();
            }
            return p.toColor().awt();
        });
    }

    // crop the image with width and height
    public static ImmutableImage crop(ImmutableImage image, int width, int height) {
        return image.resizeTo(width, height);
    }

    public static ImmutableImage rotate(ImmutableImage image, int degree) {
        // not sure it use 2 is right!!
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
