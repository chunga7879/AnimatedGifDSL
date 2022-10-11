package parser;

import core.Scope;
import core.checkers.StaticChecker;
import core.values.Function;
import core.values.Value;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Pair;

public final class GifDSLCompiler {
    private boolean enableStaticCheck;
    private boolean verbose;
    private Scope scope;

    public GifDSLCompiler() {
        this.enableStaticCheck = true;
        this.verbose = true;
        this.scope = new Scope();
    }

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
    public void addPredefinedValues(String name, Value value) {
        this.scope.setVar(name.toLowerCase(), value);
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
        GifDSLConverter converter = new GifDSLConverter(scope);
        Function main = converter.convertProgram(parser.program());
        print("Finished AST conversion");

        if (enableStaticCheck) {
            print("Started static checker");
            new StaticChecker().visit(scope.newChildScope(), main);
            print("Finished static checker");
        }

        print("Finished compilation");
        return new Pair<>(main, scope);
    }

    private void print(String msg) {
        if (verbose) System.out.println("[Gif DSL Compiler] " + msg);
    }

}
