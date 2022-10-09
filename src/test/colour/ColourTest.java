package colour;

import core.values.Colour;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ColourTest {

    @Test
    public void createRGBColourSuccessfully() {
        Colour colour = new Colour(100, 200, 210);
        Assertions.assertTrue(colour.getR() == 100);
        Assertions.assertTrue(colour.getG() == 200);
        Assertions.assertTrue(colour.getB() == 210);
    }

    @Test
    public void createRGBColourUnsuccessfully() {
        try {
            Colour colour = new Colour(100, 300, 210);
            Assertions.assertFalse(true);
        } catch (IllegalArgumentException exception) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void createHexColourSuccessfully() {
        Colour colour = new Colour("#A234FF");
        Assertions.assertTrue(colour.getR() == 162);
        Assertions.assertTrue(colour.getG() == 52);
        Assertions.assertTrue(colour.getB() == 255);
    }

    @Test
    public void createHexColourUnsuccessfully() {
        try {
            Colour colour = new Colour("#ZKKKKK");
            Assertions.assertFalse(true);
        } catch (IllegalArgumentException exception) {
            Assertions.assertTrue(true);
        }
    }
}
