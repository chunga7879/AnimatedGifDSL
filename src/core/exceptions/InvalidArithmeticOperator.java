package core.exceptions;

import core.values.Value;

public class InvalidArithmeticOperator extends DSLException {
    private final String type;
    private final String op;

    public InvalidArithmeticOperator(Value v) {
        this.type = v.getTypeName();
        this.op = "";
    }

    public InvalidArithmeticOperator(Value v, String op) {
        this.type = v.getTypeName();
        this.op = op;
    }

    @Override
    protected String getDetails() {
        if (!this.op.isEmpty()) {
            return this.type + " does not support operator " + this.op;
        }
        return this.type + " does not support arithmetic operators.";
    }
}
