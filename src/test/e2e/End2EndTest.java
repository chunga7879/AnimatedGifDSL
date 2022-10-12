package e2e;

import builtin.functions.Print;
import core.Scope;
import core.evaluators.Evaluator;
import core.statements.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

public class End2EndTest {

    @Test
    public void testPrintProgram() {
        String input = """
            DEFINE PRINT2 WITH (msg2):
              PRINT msg2
            LOOP i IN (1, 3):
              IF (i = 2):
                PRINT "if"
              PRINT "loop"
            PRINT "print"
            PRINT2
              WITH msg2: "func"
            """;
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.addPredefinedValues("print", new Print());
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
    }

    /**
     * Test that user can call a user-defined function from inside a user-defined function
     */
    @Test
    public void testCallUserDefinedFunctionInUserDefinedFunction() {
        String input = """
            DEFINE PRINT2 WITH (msg):
              PRINT "call print2"
              PRINT msg
            DEFINE PRINT3 WITH (msg):
              PRINT "call print3"
              PRINT2
                WITH msg: msg
            PRINT3
              WITH msg: "func"
            """;
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.addPredefinedValues("print", new Print());
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
    }
}
