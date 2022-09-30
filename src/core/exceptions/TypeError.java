package core.exceptions;

public class TypeError extends DSLException {
    private final String have;
    private final String want;

    public TypeError(String have, String want) {
        this.have = have;
        this.want = want;
    }

    @Override
    protected String getDetails() {
        return "have type " + this.have + " wanted " + this.want;
    }
}
