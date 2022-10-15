package core.exceptions;

/**
 * Exception due to bad statement (e.g. bad statement location)
 */
public class StatementException extends DSLException {
    public StatementException(String msg) {
        super(msg);
    }
}
