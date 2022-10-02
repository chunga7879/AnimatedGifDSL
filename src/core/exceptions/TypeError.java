package core.exceptions;

import core.values.Value;

public class TypeError extends DSLException {
    private final String have;
    private final String want;

    public TypeError(Value have, String want) {
        this.have = have.getTypeName();
        this.want = want;
    }

    @Override
    protected String getDetails() {
        return "have type " + this.have + " wanted " + this.want;
    }
}
