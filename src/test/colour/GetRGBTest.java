package colour;

import builtin.functions.colour.GetB;
import builtin.functions.colour.GetG;
import builtin.functions.colour.GetR;
import core.Scope;
import core.values.Colour;
import core.values.IntegerValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GetRGBTest {
    @Test
    public void getRSuccessfully() {
        Scope scope = new Scope();
        scope.setVar("$target", new Colour(100, 200, 230));
        GetR createColour = new GetR();
        IntegerValue rValue = (IntegerValue) createColour.call(scope);
        Assertions.assertEquals(100, rValue.get());
    }

    @Test
    public void getBSuccessfully() {
        Scope scope = new Scope();
        scope.setVar("$target", new Colour(100, 200, 230));
        GetB createColour = new GetB();
        IntegerValue rValue = (IntegerValue) createColour.call(scope);
        Assertions.assertEquals(230, rValue.get());
    }

    @Test
    public void getGSuccessfully() {
        Scope scope = new Scope();
        scope.setVar("$target", new Colour(100, 200, 230));
        GetG createColour = new GetG();
        IntegerValue rValue = (IntegerValue) createColour.call(scope);
        Assertions.assertEquals(200, rValue.get());
    }
}
