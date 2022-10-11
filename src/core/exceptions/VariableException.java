package core.exceptions;

public class VariableException extends DSLException {
    private final String msg;

    public VariableException(String msg) {
        this.msg = msg;
    }

    @Override
    public String message() {
        return this.msg;
    }
}
