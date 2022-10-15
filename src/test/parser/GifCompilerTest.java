package parser;

import core.Scope;
import core.statements.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
            LOOP i IN (1, 20):
              PRINT "hi1"
              LOOP j IN array:
                PRINT "hi2"
            LOOP i IN (20, 1):
              PRINT "hi3"
            PRINT "hi4"
            """;
        compile(input);
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
        } catch (DSLParserException e) {
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
            } catch (DSLParserException e) {
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
        } catch (DSLParserException e) {
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
        } catch (DSLParserException e) {
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
        } catch (DSLParserException e) {
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
