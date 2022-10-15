package parser;

import core.exceptions.DSLException;

/**
 * Exception that occurs during Tokenization, Parsing, or AST conversion
 */
public class DSLParserException extends DSLException {
    public DSLParserException(String message) {
        super(message);
    }
}
