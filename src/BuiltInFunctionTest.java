import builtin.functions.ImageManipulation;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.nio.PngWriter;

import java.io.File;
import java.io.IOException;

public class BuiltInFunctionTest {

    public static void main(String[] args) throws IOException {

        // test resize
        ImmutableImage dvd = ImmutableImage.loader().fromFile("dvd-logo.png");

        dvd = ImageManipulation.resize(dvd, 100, 200);

        dvd.output(PngWriter.NoCompression, new File("dvd-test-resize.png"));

        // test crop
        dvd = ImmutableImage.loader().fromFile("dvd-logo.png");
        dvd = ImageManipulation.crop(dvd, 1000, 300);
        dvd.output(PngWriter.NoCompression, new File("dvd-test-crop.png"));

        // test crop
        dvd = ImmutableImage.loader().fromFile("dvd-logo.png");
        dvd = ImageManipulation.rotate(dvd, 90);
        dvd.output(PngWriter.NoCompression, new File("dvd-test-rotate-90.png"));

        dvd = ImmutableImage.loader().fromFile("dvd-logo.png");
        dvd = ImageManipulation.rotate(dvd, 45);
        dvd.output(PngWriter.NoCompression, new File("dvd-test-rotate-45.png"));

    }
}
