package parser;

import core.Scope;
import core.statements.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GifCompilerTest {

    @Test
    public void testRequireNewLineAtEnd() {
        List<String> inputs = new ArrayList<>() {{
            add("DO hello");
            add("DO hello\n// hello");
            add("Do hello\n     ");
        }};
        for (String input : inputs) {
            try {
                compile(input);
                Assertions.fail("Requires new line at the end");
            } catch (DSLParserException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void testEmptyLinesAndNewLines() {
        String newLineAtStartAndEnd = "\nDO hello\n";
        compile(newLineAtStartAndEnd);

        String newLineAtStartAndEnd2 = "\r\n\r\nDO hello\r\n\r\n";
        compile(newLineAtStartAndEnd2);

        String noNewLineAtStart = "DO hello\r\n";
        compile(noNewLineAtStart);

        String emptyLinesBetween = "DO hello\r\n\r\n\r\nDO something\r\n";
        compile(emptyLinesBetween);

        String emptyLinesWithSpaceBetween = "DO hello\r\n  \r\n  \r\nDO something\r\n";
        compile(emptyLinesWithSpaceBetween);

        String newLineAtStartAndEndWithEmptyLine = "  \nDo hello  \n    \n";
        compile(newLineAtStartAndEndWithEmptyLine);

        String lotsOfSpaceBetween = "     IF   (    x     >      hello   )   :  \n";
        compile(lotsOfSpaceBetween);
    }

    @Test
    public void testSpaces() {
        String lotsOfSpaceBetween = "     IF   (    x     >      hello   )   :  \n";
        Program program = compile(lotsOfSpaceBetween).a;
        Assertions.assertEquals(1, program.statements().size());
        Assertions.assertTrue(program.statements().get(0) instanceof IfStatement);
    }

    @Test
    public void testComments() {
        String input = """
            // comment 1
            DEFINE func1 b WITH (c):
              // comment 2
              IF (x > 2):
                               // comment 3
                DO a
                     // comment 4
            // comment 5
                        //comment 6
            """;
        Program program = compile(input).a;
        Assertions.assertEquals(1, program.statements().size());
        Assertions.assertTrue(program.statements().get(0) instanceof FunctionDefinition);
        FunctionDefinition funcDef = (FunctionDefinition) program.statements().get(0);
        Assertions.assertEquals(2, funcDef.statements().size());
        Assertions.assertTrue(funcDef.statements().get(0) instanceof VariableAssignment);
        Assertions.assertTrue(funcDef.statements().get(1) instanceof IfStatement);
        IfStatement ifStatement = (IfStatement) funcDef.statements().get(1);
        Assertions.assertEquals(1, ifStatement.statements().size());
        Assertions.assertTrue(ifStatement.statements().get(0) instanceof ExpressionWrapper);
    }

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

    private Pair<Program, Scope> compile(String input) {
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.setEnableStaticChecker(false);
        return compiler.compile(CharStreams.fromString(input));
    }

    // TODO: add more tests
}
