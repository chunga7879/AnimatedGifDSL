package core.exceptions;

public class NameError extends DSLException {
    private final String name;

    public NameError(String name) {
        this.name = name;
    }

    @Override
    protected String getDetails() {
        return "name '"+this.name+"' is not defined";
    }
}
