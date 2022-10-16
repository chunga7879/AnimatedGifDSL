package image;

import builtin.functions.GetHeight;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class GetHeightTest {

    @Test
    public void getHeightSuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        ImmutableImage dvd = FileSystem.openImage("src/test/image/inputs/dvd-logo.png");
        scope.setVar(AbstractFunction.PARAM_TARGET, new Image(dvd));
        GetHeight getHeight = new GetHeight();
        IntegerValue height = (IntegerValue) getHeight.call(scope);

        Assertions.assertEquals(dvd.height, height.get());
    }
}
