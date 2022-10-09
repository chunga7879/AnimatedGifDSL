package test.e2e;

import builtin.functions.Print;
import core.Scope;
import core.values.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

public class End2EndTest {

    @Test
    public void testPrintProgram() {
        String input = """
            DEFINE PRINT2 WITH (msg2):
              PRINT
                WITH msg: msg2
            LOOP i IN (1, 3):
              IF (i = 2):
                PRINT
                  WITH msg: "if"
              PRINT
                WITH msg: "loop"
            PRINT
              WITH msg: "print"
            PRINT2
              WITH msg2: "func"
            """;
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.setEnableStaticChecker(false);
        compiler.addPredefinedValues("print", new Print());
        Pair<Function, Scope> main = compiler.compile(CharStreams.fromString(input));
        main.a.call(main.b);
    }
}
