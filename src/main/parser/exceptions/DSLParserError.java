package parser.exceptions;

import core.exceptions.DSLException;

/**
 * Exception that occurs during parsing
 */
public class DSLParserError extends DSLException {
    public DSLParserError(String message) {
        super(message);
    }
}
