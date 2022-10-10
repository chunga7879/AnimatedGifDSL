package core.exceptions;

public class FunctionException extends DSLException {
    private final String msg;

    public FunctionException(String msg) {
        this.msg = msg;
    }

    @Override
    public String message() {
        return this.msg;
    }
}
