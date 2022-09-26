import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.color.RGBColor;
import com.sksamuel.scrimage.nio.GifSequenceWriter;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScrimageExample {
    private static int Random(Random random, int min, int max) {
        return min + random.nextInt((max - min) + 1);
    }

    private static ImmutableImage Color(ImmutableImage image, int r, int g, int b) {
        return image.map((p) -> {
            if (p.alpha() != 0) {
                return new RGBColor(r, g, b, p.alpha()).awt();
            }
            return p.toColor().awt();
        });
    }

    private static ImmutableImage InvertColor(ImmutableImage image) {
        return image.map((p) -> new RGBColor(255 - p.red(), 255 - p.green(), 255 - p.blue()).awt());
    }

    private static ImmutableImage RandomColor(Random random, ImmutableImage image) {
        int r = Random(random, 0, 200);
        int g = Random(random, 0, 200);
        int b = Random(random, 0, 200);
        return Color(image, r, g, b);
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Starting...");

        Random random = new Random();

        ImmutableImage dvd = ImmutableImage.loader().fromFile("dvd-logo.png");
        dvd = dvd.scaleTo(40, 20);
        dvd = RandomColor(random, dvd);

        ImmutableImage background = ImmutableImage.filled(400, 400, Color.WHITE);

        List<ImmutableImage> frames = new ArrayList<>();

        int x = (background.width / 2) - (dvd.width / 2);
        int y = (background.height / 2) - (dvd.height / 2);
        int dx = Random(random, 1, 10);
        int dy = 11 - dx;

        for (int i = 1; i <= 360; i++) {
            x = x + dx;
            y = y + dy;
            if (x < 0) {
                x = 0;
                dx = dx * -1;
                dvd = RandomColor(random, dvd);
            }
            if (x > background.width - dvd.width) {
                x = background.width - dvd.width;
                dx = dx * -1;
                dvd = RandomColor(random, dvd);
            }
            if (y < 0) {
                y = 0;
                dy = dy * -1;
                dvd = RandomColor(random, dvd);
            }
            if (y > background.height - dvd.height) {
                y = background.height - dvd.height;
                dy = dy * -1;
                dvd = RandomColor(random, dvd);
            }
            ImmutableImage frame = background.copy().overlay(dvd, x, y);
            frames.add(frame);
        }

        List<ImmutableImage> editedFrames = new ArrayList<>();

        for (ImmutableImage f: frames) {
            f = InvertColor(f);
            editedFrames.add(f);
        }

        System.out.println("Processing...");

        int duration = 4;
        GifSequenceWriter gifWriter = new GifSequenceWriter(Math.round(((double) duration * 1000) / frames.size()), false);
        gifWriter.output(editedFrames.toArray(ImmutableImage[]::new), "final.gif");

        System.out.println("Finished!");
    }
}