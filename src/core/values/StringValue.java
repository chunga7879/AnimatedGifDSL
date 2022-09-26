package core.values;

public class StringValue extends Value {
    public static final String NAME = "string";

    private final String s;

    public StringValue(String s) {
        super(StringValue.NAME);
        this.s = s;
    }

    public String get() {
        return this.s;
    }

    @Override
    public StringValue asString() {
        return this;
    }
}
