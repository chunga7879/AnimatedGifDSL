package utils;

import core.values.Colour;

import java.util.List;
import java.util.stream.Stream;

public enum ColourConstant {
    BLACK ("black", 0, 0, 0),
    GREY ("grey", 128, 128, 128),
    WHITE ("white", 255, 255, 255),
    RED ("red", 255, 0, 0),
    ORANGE ("orange", 255, 127, 0),
    YELLOW ("yellow", 255, 255, 0),
    GREEN ("green", 0, 255, 0),
    BLUE ("blue", 0, 0, 255),
    CYAN ("cyan", 0, 255, 255),
    MAGENTA ("magenta", 255, 0, 255),
    PURPLE ("indigo", 128, 0, 128);

    private final String name;
    private final int r;
    private final int g;
    private final int b;

    ColourConstant(String name, int r, int g, int b) {
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public String getName() {
        return name;
    }

    public Colour createColour() {
        return new Colour(r, g, b);
    }

    public static List<String> getNameList() {
        return Stream.of(ColourConstant.values()).map(ColourConstant::getName).toList();
    }

}
