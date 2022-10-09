package core.exceptions;

import core.values.Value;

public class InvalidOperation extends DSLException {
    private final String type;
    private final String op;

    public InvalidOperation(Value v) {
        this.type = v.getTypeName();
        this.op = "";
    }

    public InvalidOperation(Value v, String op) {
        this.type = v.getTypeName();
        this.op = op;
    }

    @Override
    protected String getDetails() {
        if (!this.op.isEmpty()) {
            return this.type + " is not supported with " + this.op;
        }
        return this.type + " is not supported with the operator.";
    }
}
