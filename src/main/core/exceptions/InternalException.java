package core.exceptions;

/* An exception that is not the user's fault */
public class InternalException extends DSLException {
    public InternalException(String msg) {
        super(msg);
    }
}
