package files.filesystem;

import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.nio.PngWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileSystem {
    public static ImmutableImage openImage(String filePath) throws FileNotFoundException {
        try {
            ImmutableImage image = ImmutableImage.loader().fromFile(filePath);
            return image;
        } catch (IOException io) {
            throw new FileNotFoundException();
        }
    }

    // for test
    public static void saveImage(ImmutableImage image, String filePath) throws FileNotFoundException {
        try {
            image.output(PngWriter.NoCompression, new File(filePath));
        } catch (IOException io) {
            throw new Error("file path not found");
        }
    }
}
