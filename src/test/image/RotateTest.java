package image;

import builtin.functions.Rotate;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class RotateTest {
    @Test
    public void rotateSuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        ImmutableImage dvd = FileSystem.openImage("src/test/image/inputs/dvd-logo.png");
        scope.setVar(AbstractFunction.PARAM_TARGET, new Image(dvd));
        scope.setVar("angle", new IntegerValue(45));

        Rotate rotate = new Rotate();

        Image resultImage = (Image) rotate.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/dvd-logo-rotate-test.png");

        Assertions.assertEquals(dvd.width, resultImage.get().width);
        Assertions.assertEquals(dvd.height, resultImage.get().height);
        Assertions.assertNotNull(resultImage.get());
    }

    @Test
    public void rotateSuccessTwo() throws FileNotFoundException {
        Scope scope = new Scope();
        ImmutableImage dvd = FileSystem.openImage("src/test/image/inputs/pumpkin.png");
        scope.setVar(AbstractFunction.PARAM_TARGET, new Image(dvd));
        scope.setVar("angle", new IntegerValue(10));

        Rotate rotate = new Rotate();

        Image resultImage = (Image) rotate.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/pumpkin-rotate-test.png");

        Assertions.assertEquals(dvd.width, resultImage.get().width);
        Assertions.assertEquals(dvd.height, resultImage.get().height);
        Assertions.assertNotNull(resultImage.get());
    }

    @Test
    public void rotateSuccessLocal() throws FileNotFoundException {
        ImmutableImage dvd = FileSystem.openImage("src/test/image/inputs/pumpkin.png");

        Rotate rotate = new Rotate();

        dvd = rotate.rotate(dvd, 180);

        FileSystem.saveImage(dvd, "src/test/image/files/pumpkin--local-rotate-test.png");
    }
}
