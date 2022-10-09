package core.values;

public class Colour extends Value {
    public static final String NAME = "Colour";

    private int r;
    private int g;
    private int b;

    public Colour(int r, int g, int b) {
        super(NAME);

        if (r < 0 || g < 0 || b < 0 || r > 255 || g > 255 || b > 255) {
            throw new IllegalArgumentException("r,g,b values out of range");
        }

        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Colour(String hex) {
        super(NAME);

        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        } else {
            throw new IllegalArgumentException("Colours in hex format should start with #");
        }

        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex colour");
        }

        this.r = Integer.parseInt(hex.substring(0, 2), 16);
        this.g = Integer.parseInt(hex.substring(2, 4), 16);
        this.b = Integer.parseInt(hex.substring(4, 6), 16);
    }

    @Override
    public Colour asColour() {
        return this;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
