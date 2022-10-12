package image;

import builtin.functions.GetWidth;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.values.AbstractFunction;
import core.values.Image;
import core.values.IntegerValue;
import files.filesystem.FileSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

class GetWidthTest {

    @Test
    public void getHeightSuccess() throws FileNotFoundException {
        Scope scope = new Scope();
        ImmutableImage dvd = FileSystem.openImage("dvd-logo.png");
        scope.setVar(AbstractFunction.PARAM_TARGET, new Image(dvd));
        GetWidth getWidth = new GetWidth();
        IntegerValue width = (IntegerValue) getWidth.call(scope);

        Assertions.assertEquals(dvd.width, width.get());
    }

}
