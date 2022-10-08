package core.values;

public class Null extends Value {
    public static final Null NULL = new Null();
    private static final String NAME = "Null";
    public Null() {
        super(Null.NAME);
    }
}
