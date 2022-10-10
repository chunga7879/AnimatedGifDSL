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
            sealImage = ImmutableImage.loader().fromFile("src/test/filtering/testInputs/italy.jpeg");
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
            Image italy = (Image) filter.call(scope);

            italy.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/sepiaItaly.png"));
        } catch (IOException exception) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void filterChrome() {
        try {
            scope.setVar("filtering", new StringValue("chrome"));

            Filter filter = new Filter();
            Image seal = (Image) filter.call(scope);

            seal.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/chromeItaly.png"));
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

            seal.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/blurItaly.png"));
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

            seal.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/greyScaleItaly.png"));
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

            seal.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/sharpenItaly.png"));
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

            seal.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/invertItaly.png"));
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
