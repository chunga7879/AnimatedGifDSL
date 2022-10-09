package files.filesystem;

import com.sksamuel.scrimage.ImmutableImage;

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
}
