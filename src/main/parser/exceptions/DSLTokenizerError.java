package parser.exceptions;

import core.exceptions.DSLException;

/**
 * Exception that occurs during tokenization
 */
public class DSLTokenizerError extends DSLException {
    public DSLTokenizerError(String message) {
        super(message);
    }
}
