package image;

import builtin.functions.Overlay;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.FileNotFoundException;

class OverlayTest {
    @Test
    public void overlaySuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        ImmutableImage dvd = FileSystem.openImage("src/test/image/inputs/dvd-logo.png");
        scope.setVar(AbstractFunction.PARAM_TARGET, new Image(dvd));

        ImmutableImage background = ImmutableImage.filled(2000, 2000, Color.WHITE);
        scope.setVar(AbstractFunction.PARAM_ON, new Image(background));
        scope.setVar("x", new IntegerValue((2000 - dvd.width) / 2));
        scope.setVar("y", new IntegerValue((2000 - dvd.height) / 2));

        Overlay overlay = new Overlay();

        Image resultImage = (Image) overlay.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/dvd-logo-overlay-test.png");

        Assertions.assertEquals(background.width, resultImage.get().width);
        Assertions.assertEquals(background.height, resultImage.get().height);
        Assertions.assertNotNull(resultImage.get());
    }
}
