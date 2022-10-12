package image;

import builtin.functions.Write;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import core.values.StringValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class WriteTest {
    @Test
    public void writeSuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        scope.setVar(AbstractFunction.PARAM_TARGET, new StringValue("Hello, Group 15"));
        scope.setVar("size", new IntegerValue(40));
        scope.setVar("font", new StringValue("Serif"));
        scope.setVar("style", new StringValue("bold"));
        scope.setVar("width", new IntegerValue(1000));
        scope.setVar("height", new IntegerValue(500));

        Write write = new Write();

        Image resultImage = (Image) write.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/write-test.png");

        Assertions.assertEquals(1000, resultImage.get().width);
        Assertions.assertEquals(500, resultImage.get().height);
        Assertions.assertNotNull(resultImage.get());
    }

    @Test
    public void writeSuccessTwo() throws FileNotFoundException {
        Scope scope = new Scope();
        scope.setVar(AbstractFunction.PARAM_TARGET, new StringValue("Hello, Group 15"));
        scope.setVar("size", new IntegerValue(40));
        // use default font if it is not available
        scope.setVar("font", new StringValue("alskdjalskdjalsda"));
        scope.setVar("style", new StringValue("bold"));
        scope.setVar("width", new IntegerValue(1000));
        scope.setVar("height", new IntegerValue(500));

        Write write = new Write();

        Image resultImage = (Image) write.call(scope);

        FileSystem.saveImage(resultImage.get(), "src/test/image/files/write-two-test.png");

        Assertions.assertEquals(1000, resultImage.get().width);
        Assertions.assertEquals(500, resultImage.get().height);
        Assertions.assertNotNull(resultImage.get());
    }
}
