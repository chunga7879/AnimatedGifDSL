package core.exceptions;

/**
 * Exception for invalid argument to function
 */
public class InvalidArgumentException extends FunctionException {
    public InvalidArgumentException(String msg) {
        super(msg);
    }
}
