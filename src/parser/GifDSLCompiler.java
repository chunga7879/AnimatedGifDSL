package parser;

import core.Scope;
import core.values.Function;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Pair;

public final class GifDSLCompiler {
    private boolean enableStaticCheck = true;
    private boolean verbose = true;

    /**
     * Set whether to run the static checker
     * @param enableStaticCheck
     */
    public void setEnableStaticChecker(boolean enableStaticCheck) {
        this.enableStaticCheck = enableStaticCheck;
    }

    /**
     * Add predefined values (constants & functions)
     */
    public void addPredefinedValues() {
    }

    /**
     * Compile the text input to the GifDSL
     * @param input
     * @return
     */
    public Pair<Function, Scope> compile(CharStream input) {
        print("Starting compilation");

        print("Starting tokenization");
        GifDSLLexer lexer = new GifDSLLexer(input);
        lexer.removeErrorListener(ConsoleErrorListener.INSTANCE);
        lexer.addErrorListener(new GifDSLErrorListener(GifDSLErrorListener.PREFIX_LEXER_ERROR));
        TokenStream tokens = new CommonTokenStream(lexer);
        print("Finished tokenization");

        print("Starting parsing");
        GifDSLParser parser = new GifDSLParser(tokens);
        parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
        parser.addErrorListener(new GifDSLErrorListener(GifDSLErrorListener.PREFIX_PARSER_ERROR));
        print("Finished parsing");

        print("Started AST conversion");
        Scope rootScope = new Scope();
        GifDSLConverter converter = new GifDSLConverter(rootScope);
        Function main = converter.convertProgram(parser.program());
        print("Finished AST conversion");

        if (enableStaticCheck) {
            print("Started static checker");
            // TODO run static check
            print("Finished static checker");
        }

        print("Finished compilation");
        return new Pair<>(main, rootScope);
    }

    private void print(String msg) {
        if (verbose) System.out.println("[Gif DSL Compiler] " + msg);
    }

}
