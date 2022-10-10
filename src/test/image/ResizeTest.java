package image;

import builtin.functions.Crop;
import builtin.functions.Resize;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.Image;
import core.values.IntegerValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class ResizeTest {
    @Test
    public void resizeSuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        ImmutableImage dvd = FileSystem.openImage("dvd-logo.png");
        scope.setVar("$target", new Image(dvd));
        scope.setVar("width", new IntegerValue(100));
        scope.setVar("height", new IntegerValue(200));

        Resize resize = new Resize();

        Image resultImage = (Image) resize.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/dvd-logo-resize-test.png");

        Assertions.assertEquals(scope.getVar("width").asInteger().get(), resultImage.get().width);
        Assertions.assertEquals(scope.getVar("height").asInteger().get(), resultImage.get().height);
    }
}
