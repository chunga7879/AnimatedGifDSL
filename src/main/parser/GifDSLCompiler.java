package parser;

import core.Scope;
import core.checkers.StaticChecker;
import core.statements.Program;
import core.values.Value;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.Pair;

public final class GifDSLCompiler {
    private boolean enableStaticCheck;
    private boolean enableShortcuts;
    private boolean verbose;
    private Scope rootScope;

    public GifDSLCompiler() {
        this.enableStaticCheck = true;
        this.enableShortcuts = true;
        this.verbose = true;
        this.rootScope = new Scope();
    }

    /**
     * Set whether to run the static checker
     * @param enableStaticCheck
     */
    public void setEnableStaticChecker(boolean enableStaticCheck) {
        this.enableStaticCheck = enableStaticCheck;
    }

    /**
     * Set whether to enable shortcuts
     * @param enableShortcuts
     */
    public void setEnableShortcuts(boolean enableShortcuts) {
        this.enableShortcuts = enableShortcuts;
    }

    /**
     * Add predefined values (constants & functions)
     */
    public void addPredefinedValues(String name, Value value) {
        this.rootScope.setVar(name.toLowerCase(), value);
    }

    /**
     * Compile the text input to the GifDSL
     * @param input
     * @return
     */
    public Pair<Program, Scope> compile(CharStream input) {
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
        GifDSLConverter converter = new GifDSLConverter(rootScope);
        Program main = converter.convertProgram(parser.program());
        print("Finished AST conversion");

        if (enableStaticCheck) {
            print("Started static checker");
            new StaticChecker().visit(rootScope.newChildScope(), main);
            if (enableShortcuts) {
                main = new ShortcutsProcessor().visit(rootScope.newChildScope(), main);
                new StaticChecker().visit(rootScope.newChildScope(), main);
            }
            print("Finished static checker");
        }

        print("Finished compilation");
        return new Pair<>(main, rootScope);
    }

    private void print(String msg) {
        if (verbose) System.out.println("[Gif DSL Compiler] " + msg);
    }

}
