package parser;

import core.Scope;
import core.statements.ExpressionWrapper;
import core.statements.LoopStatement;
import core.statements.Program;
import core.values.Array;
import core.values.IntegerValue;
import core.values.Value;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.exceptions.DSLConverterError;

import java.util.ArrayList;
import java.util.List;

public class GifCompilerTest {

    @Test
    public void testCompileDefine() {
        String input = """
            DEFINE func1 b WITH (c):
              DO a
                WITH x: a
                WITH y: b
              IF (x > 2):
                DO b
                  WITH x: a
                  WITH y: b
              RETURN a
            DEFINE func2 b:
              RETURN a
            DEFINE func3 WITH (b):
              RETURN a
            DEFINE func4:
              RETURN a
            """;
        compile(input);
    }

    @Test
    public void testCompileIf() {
        String input = """
            IF (a + i > b):
              IF (2 < c):
                IF (d >= 4):
              PRINT "hi1"
              IF (10 = 100 + b):
                PRINT "hi2"
                IF (e <= -10):
                  PRINT "hi3"
              IF (0 + 10 != 2):
                PRINT "hi4"
              IF (x + y < 2 + 4):
                PRINT "hi5"
            PRINT "hi6"
            """;
        compile(input);
    }

    @Test
    public void testCompileLoop() {
        String input = """
            LOOP i IN 1 TO 20:
              PRINT "hi1"
              LOOP j IN array:
                PRINT "hi2"
            LOOP i-x IN 100 TO -4:
              PRINT "hi3"
            PRINT "hi4"
            """;
        Program program = compile(input).a;
        Assertions.assertEquals(3, program.statements().size());

        Assertions.assertTrue(program.statements().get(0) instanceof LoopStatement);
        Assertions.assertTrue(program.statements().get(1) instanceof LoopStatement);
        Assertions.assertTrue(program.statements().get(2) instanceof ExpressionWrapper);
        Assertions.assertTrue(program.statements().get(2) instanceof ExpressionWrapper);

        LoopStatement loop1 = (LoopStatement) program.statements().get(0);
        LoopStatement loop2 = (LoopStatement) program.statements().get(1);

        Assertions.assertEquals("i", loop1.loopVar());
        Assertions.assertEquals(2, loop1.statements().size());
        Assertions.assertTrue(loop1.statements().get(0) instanceof ExpressionWrapper);
        Assertions.assertTrue(loop1.statements().get(1) instanceof LoopStatement);
        Array array1 = (Array) loop1.array();
        Assertions.assertEquals(20, array1.get().size());
        int i = 1;
        for (Value v : array1) {
            Assertions.assertTrue(v instanceof IntegerValue);
            Assertions.assertEquals(i, ((IntegerValue) v).get());
            i++;
        }

        Assertions.assertEquals("i-x", loop2.loopVar());
        Assertions.assertEquals(1, loop2.statements().size());
        Assertions.assertTrue(loop2.statements().get(0) instanceof ExpressionWrapper);
        Array array2 = (Array) loop2.array();
        Assertions.assertEquals(105, array2.get().size());
        int i_x = 100;
        for (Value v : array2) {
            Assertions.assertTrue(v instanceof IntegerValue);
            Assertions.assertEquals(i_x, ((IntegerValue) v).get());
            i_x--;
        }
    }

    @Test
    public void testCompileFunctionCalls() {
        String input = """
            DO
            DO x
            DO x as x
            DO a ON b as x
            DO x as x
              WITH a: 10
              WITH B: "hi"
              WITH c: array
            DO "hello"
              WITH a1: -100 + 2
              WITH b2: i + 2
              WITH c3: 0 + x
              WITH d4: a + b
            DO 12
              WITH a: 100000
              WITH colour: #FF1122
            DO #001122
            """;
        compile(input);
    }

    @Test
    public void testNonWithStatementInFunctionCall() {
        String input = """
            DO x
              IF (x > 0):
                PRINT "hi"
            """;
        try {
            compile(input);
            Assertions.fail("Should not allow non-with statements inside of function calls");
        } catch (DSLConverterError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(2, e.getLinePosition());
            Assertions.assertEquals(0, e.getColumnPosition());
        }
    }

    @Test
    public void testWithOnBaseLevel() {
        List<String> inputs = new ArrayList<>() {{
            add("""
            PRINT "a"
            PRINT "hello"
            WITH x: 0
            """);
            add("""
            IF (x > 0):
              PRINT "hello"
              WITH x: 0
            """);
            add("""
            LOOP x IN (1, 2):
              PRINT "hello"
              WITH x: 0
            """);
            add("""
            DEFINE func:
              PRINT "hello"
              WITH x: 0
            """);
        }};
        for (String input : inputs) {
            try {
                compile(input);
                Assertions.fail("Should not allow with statements outside of function calls");
            } catch (DSLConverterError e) {
                System.out.println(e.getMessage());
                Assertions.assertEquals(3, e.getLinePosition());
                Assertions.assertEquals(0, e.getColumnPosition());
            }
        }
    }

    @Test
    public void testBadColorFormat() {
        String input = """
            DO #FF00FF00FF
            """;
        try {
            compile(input);
            Assertions.fail("Should not allow bad colour format");
        } catch (DSLConverterError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(1, e.getLinePosition());
            Assertions.assertEquals(3, e.getColumnPosition());
        }
    }

    @Test
    public void testAboveMaxInteger() {
        String input = """
            DO 2147483648
            """;
        try {
            compile(input);
            Assertions.fail("Should not allow above max integer");
        } catch (DSLConverterError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(1, e.getLinePosition());
            Assertions.assertEquals(3, e.getColumnPosition());
        }
    }

    @Test
    public void testBadIntegerInLoop() {
        String input = """
            LOOP i IN (-2147483649, 3):
              DO i
            """;
        try {
            compile(input);
            Assertions.fail("Should not allow above max integer");
        } catch (DSLConverterError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(1, e.getLinePosition());
            Assertions.assertEquals(11, e.getColumnPosition());
        }
    }

    private Pair<Program, Scope> compile(String input) {
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.setEnableStaticChecker(false);
        return compiler.compile(CharStreams.fromString(input));
    }

    // TODO: add more tests
}
