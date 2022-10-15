package parser.exceptions;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * ErrorListener to throw exceptions on Parser error
 * <a href="https://stackoverflow.com/a/26573239">https://stackoverflow.com/a/26573239</a>
 */
public class DSLParserErrorListener extends BaseErrorListener {
    @Override
    public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new DSLParserError(msg).withPosition(line, charPositionInLine);
    }
}
