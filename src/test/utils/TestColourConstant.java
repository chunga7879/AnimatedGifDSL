package utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestColourConstant {
    @Test
    public void testGetNameList() {
        List<String> actualNames = ColourConstant.getNameList();
        assertEquals(ColourConstant.values().length, actualNames.size());
        List<String> expectedNames = new ArrayList<>(Arrays.asList("red", "green", "blue"));
        for (String s: expectedNames) {
            assertTrue(actualNames.contains(s));
        }
    }

}
