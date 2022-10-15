package e2e;

import builtin.functions.Print;
import builtin.functions.Random;
import builtin.functions.Set;
import core.Scope;
import core.evaluators.Evaluator;
import core.exceptions.DSLException;
import core.exceptions.NameError;
import core.statements.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

public class End2EndTest {
    private GifDSLCompiler compiler;
    private Evaluator evaluator;

    @BeforeEach
    public void runBefore() {
        compiler = new GifDSLCompiler();
        compiler.addPredefinedValues(Print.ACTUAL_NAME, new Print());
        compiler.addPredefinedValues(Set.ACTUAL_NAME, new Set());
        compiler.addPredefinedValues(Random.ACTUAL_NAME, new Random());
        compiler.setEnableStaticChecker(true);
        compiler.setEnableShortcuts(true);
        evaluator = new Evaluator();
    }

    @Test
    public void testPrintProgram() {
        String input = """
            DEFINE PRINT2 WITH (msg2):
              PRINT msg2
            LOOP i IN 1 TO 3:
              IF (i = 2):
                PRINT "if"
              PRINT "loop"
            PRINT "print"
            PRINT2
              WITH msg2: "func"
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        evaluator.visit(main.b, main.a);
    }

    @Test
    public void testUserDefinedFunction() {
        String input = """
            SET 1 AS a
            SET 2 AS b
            SET 3 AS c
            DEFINE func x WITH (a, b):
              IF (x > 0):
                SET a + 2 AS a
              RETURN a + b
            func a + 100 as d
              WITH a: 4
              WITH b: 5
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("a"));
        Assertions.assertTrue(main.b.hasVar("b"));
        Assertions.assertTrue(main.b.hasVar("c"));
        Assertions.assertTrue(main.b.hasVar("d"));
        Assertions.assertFalse(main.b.hasVar("x"));
        Assertions.assertEquals(1, main.b.getVar("a").asInteger().get());
        Assertions.assertEquals(2, main.b.getVar("b").asInteger().get());
        Assertions.assertEquals(3, main.b.getVar("c").asInteger().get());
        Assertions.assertEquals(11, main.b.getVar("d").asInteger().get());
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
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        evaluator.visit(main.b, main.a);
    }

    @Test
    public void testUserDefinedParameterVariableWithSameName() {
        String input = """
            SET 1 AS a
            DEFINE FUNC WITH (a, b):
              SET 2 AS a
              SET 200 AS b
              RETURN a + b
            SET 100 AS b
            FUNC AS c
              WITH a: 3
              WITH b: 4
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("a"));
        Assertions.assertTrue(main.b.hasVar("b"));
        Assertions.assertTrue(main.b.hasVar("c"));
        Assertions.assertEquals(1, main.b.getVar("a").asInteger().get());
        Assertions.assertEquals(100, main.b.getVar("b").asInteger().get());
        Assertions.assertEquals(202, main.b.getVar("c").asInteger().get());
    }

    @Test
    public void testUserDefinedParameterVariableWithSameNameUnchanged() {
        String input = """
            SET 1 AS a
            DEFINE FUNC WITH (a, b):
              RETURN a + b
            SET 100 AS b
            FUNC AS c
              WITH a: 3
              WITH b: 4
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("a"));
        Assertions.assertTrue(main.b.hasVar("b"));
        Assertions.assertTrue(main.b.hasVar("c"));
        Assertions.assertEquals(1, main.b.getVar("a").asInteger().get());
        Assertions.assertEquals(100, main.b.getVar("b").asInteger().get());
        Assertions.assertEquals(7, main.b.getVar("c").asInteger().get());
    }

    @Test
    public void testBuiltInParameterVariableWithSameName() {
        String input = """
            SET 0 AS min
            SET 100 AS max
                        
            RANDOM AS rand
              WITH min: min + 100
              WITH max: max + 100
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("min"));
        Assertions.assertTrue(main.b.hasVar("max"));
        Assertions.assertTrue(main.b.hasVar("rand"));
        Assertions.assertEquals(0, main.b.getVar("min").asInteger().get());
        Assertions.assertEquals(100, main.b.getVar("max").asInteger().get());
        Assertions.assertTrue(100 <= main.b.getVar("rand").asInteger().get());
        Assertions.assertTrue(200 >= main.b.getVar("rand").asInteger().get());
    }

    @Test
    public void testUserDefinedParameterVariableWithSameNameDifferentType() {
        String input = """
            SET "hi" AS a
            DEFINE FUNC WITH (a, b):
              RETURN a + b
            SET "hello" AS b
            FUNC AS c
              WITH a: 3
              WITH b: 4
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("a"));
        Assertions.assertTrue(main.b.hasVar("b"));
        Assertions.assertTrue(main.b.hasVar("c"));
        Assertions.assertEquals("hi", main.b.getVar("a").asString().get());
        Assertions.assertEquals("hello", main.b.getVar("b").asString().get());
        Assertions.assertEquals(7, main.b.getVar("c").asInteger().get());
    }

    @Test
    public void testLoopVariableWithSameNameAsVariable() {
        String input = """
            SET 0 AS x
            SET 100 AS i
            LOOP i in 1 to 10:
              SET i + x AS x
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("i"));
        Assertions.assertTrue(main.b.hasVar("x"));
        Assertions.assertEquals(100, main.b.getVar("i").asInteger().get());
        Assertions.assertEquals(55, main.b.getVar("x").asInteger().get());
    }

    @Test
    public void testVariableUndefined() {
        String input = """
            IF (0 != 0):
              SET 100 AS x
            SET x + 2 AS a
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        try {
            evaluator.visit(main.b, main.a);
            Assertions.fail("Should not allow usage of undefined variable x");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof NameError);
            Assertions.assertFalse(main.b.hasVar("x"));
            Assertions.assertFalse(main.b.hasVar("a"));
            Assertions.assertEquals(3, e.getLinePosition());
            Assertions.assertEquals(4, e.getColumnPosition());
        }
    }

    @Test
    public void testUserDefinedTargetParameterVariableWithSameName() {
        String input = """
            DEFINE FUNC a:
              RETURN a + 2
            SET 10 AS a
            FUNC 10 + 8 AS b
            """;
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.addPredefinedValues(Set.ACTUAL_NAME, new Set());
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("a"));
        Assertions.assertTrue(main.b.hasVar("b"));
        Assertions.assertEquals(10, main.b.getVar("a").asInteger().get());
        Assertions.assertEquals(20, main.b.getVar("b").asInteger().get());
    }

    @Test
    public void testEditConstant() {
        String input = """
            CREATE-COLOUR AS black
              WITH r: 0
              WITH g: 0
              WITH b: 0
            """;
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.addPredefinedValues(Set.ACTUAL_NAME, new Set());
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow editing of constant");
        } catch (DSLException e) {
            // expected
        }
    }
}
