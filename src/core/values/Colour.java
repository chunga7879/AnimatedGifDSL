package core.values;

public class Colour extends Value {
    public static final String NAME = "Colour";

    private byte r;
    private byte g;
    private byte b;

    public Colour(int r, int g, int b) {
        super(NAME);

        if (r < 0 || g < 0 || b < 0) {
            throw new IllegalArgumentException("r,g,b values out of range");
        }
    }

    public Colour(String hex) {
        super(NAME);

        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        if (hex.length() != 6) {
            throw new IllegalArgumentException("Invalid hex colour");
        }

        this.r = (byte) Integer.parseInt(hex.substring(0, 2), 16);
        this.g = (byte) Integer.parseInt(hex.substring(2, 4), 16);
        this.b = (byte) Integer.parseInt(hex.substring(4, 6), 16);
    }

    @Override
    public Colour asColour() {
        return this;
    }

    public byte getR() {
        return this.r;
    }

    public byte getG() {
        return g;
    }

    public byte getB() {
        return b;
    }
}
