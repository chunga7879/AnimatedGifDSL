package core.exceptions;

public class NameError extends DSLException {
    private final String name;

    public NameError(String name) {
        this.name = name;
    }

    @Override
    protected String getDetails() {
        return "Variable \"" + this.name + "\" is not defined";
    }
}
