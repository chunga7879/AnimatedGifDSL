package parser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

/**
 * ErrorListener to throw exceptions on Parser error
 * <a href="https://stackoverflow.com/a/26573239">https://stackoverflow.com/a/26573239</a>
 */
public class GifDSLErrorListener extends BaseErrorListener {
    public static final String PREFIX_LEXER_ERROR = "Tokenization Error";
    public static final String PREFIX_PARSER_ERROR = "Parser Error";
    private final String prefix;

    public GifDSLErrorListener(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        throw new DSLParserException(prefix + " [Line " + line + ":" + charPositionInLine + "] " + msg);
    }
}
