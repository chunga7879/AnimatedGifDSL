package core.exceptions;

import core.values.Value;

public class TypeError extends DSLException {
    public TypeError(Value have, String want) {
        super("have type " + have.getTypeName() + " wanted " + want);
    }

    public TypeError(String details) {
        super(details);
    }
}
