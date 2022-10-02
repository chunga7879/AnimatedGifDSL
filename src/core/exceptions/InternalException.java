package core.exceptions;

/* An exception that is not the user's fault */
public class InternalException extends DSLException {
    private final String msg;
    public InternalException(String msg) {
        this.msg=msg;
    }

    @Override
    public String message() {
        return this.msg;
    }
}
