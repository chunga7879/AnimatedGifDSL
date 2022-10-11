package filtering;

import builtin.functions.ColourFill;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.nio.PngWriter;
import core.Scope;
import core.values.Colour;
import core.values.Image;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class ColourFillTest {

    ImmutableImage catImage = null;
    Scope scope = null;
    @BeforeEach
    public void beforeEach() {
        try {
            catImage = ImmutableImage.loader().fromFile("src/test/filtering/testInputs/cat.png");
            Image cat = new Image(catImage);
            scope = new Scope();
            scope.setVar("$target", cat);
        } catch (IOException ioException) {
            Assertions.fail(ioException.getMessage());
        }
    }

    @Test
    public void colourFillBlack() {
        try {
            scope.setVar("colour", new Colour(0, 0, 0));

            ColourFill colourFill = new ColourFill();
            Image blackCat = (Image) colourFill.call(scope);

            blackCat.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/blackCat.png"));
        } catch (IOException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    @Test
    public void colourCatPurple() {
        try {
            scope.setVar("colour", new Colour("#A020F0"));

            ColourFill colourFill = new ColourFill();
            Image purpleCat = (Image) colourFill.call(scope);

            purpleCat.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/purpleCat.png"));
        } catch (IOException exception) {
            Assertions.fail(exception.getMessage());
        }
    }
}
