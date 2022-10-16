package image;

import builtin.functions.Translate;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class TranslateTest {
    @Test
    public void translateSuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        ImmutableImage dvd = FileSystem.openImage("src/test/image/inputs/dvd-logo.png");
        scope.setVar(AbstractFunction.PARAM_TARGET, new Image(dvd));
        scope.setVar("x", new IntegerValue(100));
        scope.setVar("y", new IntegerValue(200));

        Translate translate = new Translate();

        Image resultImage = (Image) translate.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/dvd-logo-translate-test.png");

        Assertions.assertEquals(dvd.width, resultImage.get().width);
        Assertions.assertEquals(dvd.height, resultImage.get().height);
        Assertions.assertNotNull(resultImage.get());
    }
}
