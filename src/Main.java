import core.Scope;
import core.values.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import parser.GifDSLCompiler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GifDSLCompiler compiler = new GifDSLCompiler();
        // TODO: add built-in functions / constants here
//        compiler.addPredefinedValues("print", new Print());
        // TODO: take input from specified file
        Pair<Function, Scope> main = compiler.compile(CharStreams.fromFileName("input.gifify"));
        main.a.call(main.b);
    }
}
