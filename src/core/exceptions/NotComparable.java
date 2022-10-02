package core.exceptions;

import core.values.Value;

public class NotComparable extends DSLException {
    private final String type;
    private final String op;

    public NotComparable(Value v) {
        this.type = v.getTypeName();
        this.op = "";
    }

    public NotComparable(Value v, String op) {
        this.type = v.getTypeName();
        this.op = op;
    }

    @Override
    protected String getDetails() {
        if (!this.op.isEmpty()) {
            return this.type + " is not comparable using " + this.op;
        }
        return this.type + " is not comparable";
    }
}
