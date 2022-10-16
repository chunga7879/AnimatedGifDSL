package parser;

import core.Scope;
import core.statements.*;
import core.values.Array;
import core.values.IntegerValue;
import core.values.Value;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import parser.exceptions.DSLConverterError;
import parser.exceptions.DSLParserError;

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
            } catch (DSLParserError e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Test
    public void testEmptyLinesAndNewLines() {
        String newLineAtStartAndEnd = "\nDO hello\n";
        String newLineAtStartAndEnd2 = "\r\n\r\nDO hello\r\n\r\n";
        String noNewLineAtStart = "DO hello\r\n";
        String emptyLinesBetween = "DO hello\r\n\r\n\r\nDO something\r\n";
        String emptyLinesWithSpaceBetween = "DO hello\r\n\r\n  \r\n  \r\nDO something\r\n \r\n\r\n\tWITH x: 0\r\n";
        String newLineAtStartAndEndWithEmptyLine = "  \nDo hello  \n    \n";
        List<Pair<String, Integer>> inputs = new ArrayList<>() {{
            add(new Pair<>(newLineAtStartAndEnd, 1));
            add(new Pair<>(newLineAtStartAndEnd2, 1));
            add(new Pair<>(noNewLineAtStart, 1));
            add(new Pair<>(emptyLinesBetween, 2));
            add(new Pair<>(emptyLinesWithSpaceBetween, 2));
            add(new Pair<>(newLineAtStartAndEndWithEmptyLine, 1));
        }};
        for (Pair<String, Integer> input : inputs) {
            Program program = compile(input.a).a;
            Assertions.assertEquals(input.b, program.statements().size());
            for (Statement statement : program.statements()) {
                Assertions.assertTrue(statement instanceof ExpressionWrapper);
            }
        }
    }

    @Test
    public void testSpaces() {
        String lotsOfSpaceBetween = "     IF   (    x     >      hello   )   :  \n";
        String lotsOfTabBetween = "\t\t\t\t\t\tIF\t\t\t(\t\t\tx\t\t\t\t>\t\t\t\thello\t\t\t)\t:\t\t\t\t\n";
        String lotsOfSpaceAndTabBetween = "  \t   IF \t  ( \t   x  \t   >  \t    hello \t  )  \t : \t  \n";
        List<String> inputs = new ArrayList<>() {{
            add(lotsOfSpaceBetween);
            add(lotsOfTabBetween);
            add(lotsOfSpaceAndTabBetween);
        }};
        for (String input : inputs) {
            Program program = compile(input).a;
            Assertions.assertEquals(1, program.statements().size());
            Assertions.assertTrue(program.statements().get(0) instanceof IfStatement);
        }
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
            LOOP x IN 1 TO 2:
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
            LOOP i IN -2147483649 TO 3:
              DO i
            """;
        try {
            compile(input);
            Assertions.fail("Should not allow above max integer");
        } catch (DSLConverterError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(1, e.getLinePosition());
            Assertions.assertEquals(10, e.getColumnPosition());
        }
    }

    private Pair<Program, Scope> compile(String input) {
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.setEnableStaticChecker(false);
        return compiler.compile(CharStreams.fromString(input));
    }

    // TODO: add more tests
}
