package filtering;

import builtin.functions.Filter;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.nio.PngWriter;
import core.Scope;
import core.values.Image;
import core.values.StringValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class FilterTest {
    ImmutableImage sealImage = null;
    Scope scope = null;
    @BeforeEach
    public void beforeEach() {
        try {
            sealImage = ImmutableImage.loader().fromFile("src/test/filter/testInputs/seal.png");
            Image seal = new Image(sealImage);
            scope = new Scope();
            scope.setVar("$target", seal);
        } catch (IOException ioException) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void filterSepia() {
        try {
            scope.setVar("filtering", new StringValue("sepia"));

            Filter filter = new Filter();
            Image seal = (Image) filter.call(scope);

            seal.get().output(PngWriter.NoCompression, new File("src/test/filter/testResults/sepiaSeal.png"));
        } catch (IOException exception) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void filterBlur() {
        try {
            scope.setVar("filtering", new StringValue("blur"));

            Filter filter = new Filter();
            Image seal = (Image) filter.call(scope);

            seal.get().output(PngWriter.NoCompression, new File("src/test/filter/testResults/blurSeal.png"));
        } catch (IOException exception) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void filterGreyscale() {
        try {
            scope.setVar("filtering", new StringValue("greyscale"));

            Filter filter = new Filter();
            Image seal = (Image) filter.call(scope);

            seal.get().output(PngWriter.NoCompression, new File("src/test/filter/testResults/greyScaleSeal.png"));
        } catch (IOException exception) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void filterSharpen() {
        try {
            scope.setVar("filtering", new StringValue("sharpen"));

            Filter filter = new Filter();
            Image seal = (Image) filter.call(scope);

            seal.get().output(PngWriter.NoCompression, new File("src/test/filter/testResults/sharpenSeal.png"));
        } catch (IOException exception) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void filterRetro() {
        try {
            scope.setVar("filtering", new StringValue("retro"));

            Filter filter = new Filter();
            Image seal = (Image) filter.call(scope);

            seal.get().output(PngWriter.NoCompression, new File("src/test/filter/testResults/retroSeal.png"));
        } catch (IOException exception) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void filterInvert() {
        try {
            scope.setVar("filtering", new StringValue("invert"));

            Filter filter = new Filter();
            Image seal = (Image) filter.call(scope);

            seal.get().output(PngWriter.NoCompression, new File("src/test/filter/testResults/invertSeal.png"));
        } catch (IOException exception) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void filterInvalid() {
        try {
            scope.setVar("filtering", new StringValue("summer"));

            Filter filter = new Filter();
            Image seal = (Image) filter.call(scope);

            Assertions.assertTrue(false);
        } catch (IllegalArgumentException exception) {
            Assertions.assertTrue(true);
        }
    }

}
