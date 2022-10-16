package filtering;

import builtin.functions.SetOpacity;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.nio.PngWriter;
import core.Scope;
import core.exceptions.InvalidArgumentException;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class SetOpacityTest {

    @Test
    public void setOpacityTest80() {
        try {
            ImmutableImage catMouth = ImmutableImage.loader().fromFile("src/test/filtering/testInputs/cat.png");
            Image catImage = new Image(catMouth);
            Scope scope = new Scope();
            scope.setVar(AbstractFunction.PARAM_TARGET, catImage);
            scope.setVar("amount", new IntegerValue(80));

            SetOpacity opacityFunction = new SetOpacity();
            Image transparentCat = (Image) opacityFunction.call(scope);

            transparentCat.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/transparentCat80.png"));
        } catch (IOException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    @Test
    public void setOpacityTest0() {
        try {
            ImmutableImage catMouth = ImmutableImage.loader().fromFile("src/test/filtering/testInputs/cat.png");
            Image catImage = new Image(catMouth);
            Scope scope = new Scope();
            scope.setVar(AbstractFunction.PARAM_TARGET, catImage);
            scope.setVar("amount", new IntegerValue(0));

            SetOpacity opacityFunction = new SetOpacity();
            Image transparentCat = (Image) opacityFunction.call(scope);

            transparentCat.get().output(PngWriter.NoCompression, new File("src/test/filtering/testResults/transparentCat.png"));
        } catch (IOException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    @Test
    public void setOpacityInvalidValue() {
        try {
            ImmutableImage catMouth = ImmutableImage.loader().fromFile("src/test/filtering/testInputs/cat.png");
            Image catImage = new Image(catMouth);
            Scope scope = new Scope();
            scope.setVar(AbstractFunction.PARAM_TARGET, catImage);
            scope.setVar("amount", new IntegerValue(110));

            SetOpacity opacityFunction = new SetOpacity();
            Image transparentCat = (Image) opacityFunction.call(scope);

            Assertions.fail("Illegal argument exception should have been thrown.");
        } catch (IOException exception) {
            Assertions.fail(exception.getMessage());
        } catch (InvalidArgumentException invalidArgumentException) {
            // Should catch InvalidArgumentException
        }
    }
}
