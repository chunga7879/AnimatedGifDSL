import core.values.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import parser.GifDSLConverter;
import parser.GifDSLLexer;
import parser.GifDSLParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GifDSLLexer lexer = new GifDSLLexer(CharStreams.fromFileName("input.gifify"));
        for (Token token : lexer.getAllTokens()) {
            if (token.getChannel() == Token.DEFAULT_CHANNEL) {
                System.out.println(token);
            }
        }
        lexer.reset();
        TokenStream tokens = new CommonTokenStream(lexer);
        System.out.println("Done tokenizing");

        GifDSLParser parser = new GifDSLParser(tokens);
        GifDSLConverter converter = new GifDSLConverter();
        Function main = converter.convertProgram(parser.program());
        System.out.println("Done parsing");
    }
}
