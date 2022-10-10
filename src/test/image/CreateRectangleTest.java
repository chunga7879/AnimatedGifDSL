package image;

import builtin.functions.CreateRectangle;
import builtin.functions.Resize;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.Colour;
import core.values.Image;
import core.values.IntegerValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class CreateRectangleTest {
    @Test
    public void createRectangleSuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        scope.setVar("width", new IntegerValue(100));
        scope.setVar("height", new IntegerValue(200));
        scope.setVar("colour", new Colour(3, 252, 252));

        CreateRectangle createRectangle = new CreateRectangle();

        Image resultImage = (Image) createRectangle.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/create-rectangle-test.png");

        Assertions.assertEquals(scope.getVar("width").asInteger().get(), resultImage.get().width);
        Assertions.assertEquals(scope.getVar("height").asInteger().get(), resultImage.get().height);
        Assertions.assertNotNull(resultImage.get());
    }
}
