package colour;

import builtin.functions.colour.CreateColour;
import core.Scope;
import core.exceptions.InvalidArgumentException;
import core.values.Colour;
import core.values.IntegerValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreateColourTest {

    @Test
    public void createColourSuccessfully() {
        Scope scope = new Scope();
        scope.setVar("r", new IntegerValue(100));
        scope.setVar("g", new IntegerValue(39));
        scope.setVar("b", new IntegerValue(40));
        CreateColour createColour = new CreateColour();
        Colour colour = (Colour) createColour.call(scope);
        Assertions.assertTrue(colour.getR() == 100);
        Assertions.assertTrue(colour.getG() == 39);
        Assertions.assertTrue(colour.getB() == 40);
    }

    @Test
    public void createColourUnsuccessfully() {
        Scope scope = new Scope();
        scope.setVar("r", new IntegerValue(100));
        scope.setVar("g", new IntegerValue(300));
        scope.setVar("b", new IntegerValue(40));
        CreateColour createColour = new CreateColour();
        try {
            Colour colour = (Colour) createColour.call(scope);
            // CREATE-COLOUR should throw an exception given invalid colour value
            Assertions.assertTrue(false);
        } catch (InvalidArgumentException invalidArgumentException) {
            Assertions.assertTrue(true);
        }
    }
}
