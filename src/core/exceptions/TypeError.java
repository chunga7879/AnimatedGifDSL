package core.exceptions;

import core.values.Value;

public class TypeError extends DSLException {
    private final String details;

    public TypeError(Value have, String want) {
        this.details = "have type " + have.getTypeName() + " wanted " + want;
    }

    public TypeError(String details) {
        this.details = details;
    }

    @Override
    protected String getDetails() {
        return this.details;
    }
}
