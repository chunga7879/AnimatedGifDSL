package image;

import builtin.functions.Crop;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class CropTest {
    @Test
    public void cropSuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        ImmutableImage dvd = FileSystem.openImage("dvd-logo.png");
        scope.setVar(AbstractFunction.PARAM_TARGET, new Image(dvd));
        scope.setVar("width", new IntegerValue(1000));
        scope.setVar("height", new IntegerValue(300));

        Crop crop = new Crop();

        Image resultImage = (Image) crop.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/dvd-logo-crop-test.png");

        Assertions.assertEquals(scope.getVar("width").asInteger().get(), resultImage.get().width);
        Assertions.assertEquals(scope.getVar("height").asInteger().get(), resultImage.get().height);
    }
}
