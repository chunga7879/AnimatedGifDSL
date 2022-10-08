package test.parser;

import core.Scope;
import core.values.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

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
            IF (a > b):
              IF (2 < c):
                IF (d >= 4):
              PRINT "hi1"
              IF (10 = 100):
                PRINT "hi2"
                IF (e <= -10):
                  PRINT "hi3"
              IF (0 != 2):
                PRINT "hi4"
            PRINT "hi5"
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
            PRINT "hi3"
            """;
        compile(input);
    }

    @Test
    public void testCompileFunctionCalls() {
        String input = """
            DO
            DO x
            DO x as x
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

    private Pair<Function, Scope> compile(String input) {
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.setEnableStaticChecker(false);
        return compiler.compile(CharStreams.fromString(input));
    }

    // TODO: add more tests
}
