package parser.exceptions;

import core.exceptions.DSLException;

/**
 * Exception that occurs during AST conversion
 */
public class DSLConverterError extends DSLException {
    public DSLConverterError(String message) {
        super(message);
    }
}
