package e2e;

import builtin.functions.Print;
import builtin.functions.Random;
import builtin.functions.Set;
import core.exceptions.*;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

public class End2EndStaticCheckerTest {
    private GifDSLCompiler compiler;

    @BeforeEach
    public void runBefore() {
        compiler = new GifDSLCompiler();
        compiler.addPredefinedValues(Print.ACTUAL_NAME, new Print());
        compiler.addPredefinedValues(Set.ACTUAL_NAME, new Set());
        compiler.addPredefinedValues(Random.ACTUAL_NAME, new Random());
    }

    @Test
    public void testUndefinedVariable() {
        String input = """
            SET 0 AS min
            SET minn AS max
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow usage of undefined variables");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof NameError);
            Assertions.assertEquals(2, e.getLinePosition());
            Assertions.assertEquals(4, e.getColumnPosition());
        }
    }

    @Test
    public void testUndefinedFunction() {
        String input = """
            SET "hi" AS x
            DO-SOMETHING "hi"
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow usage of undefined functions");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof FunctionNameException);
            Assertions.assertEquals(2, e.getLinePosition());
            Assertions.assertEquals(0, e.getColumnPosition());
        }
    }

    @Test
    public void testRedefineFunction() {
        String input = """
            SET "other" AS b
            DEFINE DO-X WITH (stuff):
              PRINT stuff
            SET "hi" AS x
            DEFINE DO-X WITH (a, otherStuff):
              PRINT stuff
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow usage of undefined functions");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof FunctionNameException);
            Assertions.assertEquals(5, e.getLinePosition());
        }
    }

    @Test
    public void testMissingParameterButHasVariable() {
        String input = """
            SET 0 AS min
            SET 100 AS max
            RANDOM AS rand
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow missing parameters");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof FunctionException);
            Assertions.assertEquals(3, e.getLinePosition());
            Assertions.assertEquals(0, e.getColumnPosition());
        }
    }

    @Test
    public void testWrongParameterNameButHasVariable() {
        String input = """
            SET 0 AS min
            SET 100 AS max
            RANDOM AS rand
              WITH min: 10
              WITH maax: 20
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow wrong parameters");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof FunctionException);
            Assertions.assertEquals(3, e.getLinePosition());
            Assertions.assertEquals(0, e.getColumnPosition());
        }
    }

    @Test
    public void testArgumentTypeException() {
        String input = """
            PRINT "hi"
            PRINT "do"
            SET 10 as x
            IF (10 > 0):
                PRINT x
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow wrong parameter type");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof FunctionException);
            Assertions.assertEquals(5, e.getLinePosition());
            Assertions.assertEquals(4, e.getColumnPosition());
        }
    }

    @Test
    public void testIfWithWrongType() {
        String input = """
            SET 100 AS i
            SET "hi" AS a
            DEFINE func WITH (x):
              IF (i > 0):
                IF (x > 0):
                  PRINT x
            func
              WITH x: a
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not if comparison with wrong type");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof TypeError);
            Assertions.assertEquals(5, e.getLinePosition());
            Assertions.assertEquals(8, e.getColumnPosition());
        }
    }

    @Test
    public void testLoopWithWrongType() {
        String input = """
            SET "hi" AS array
            LOOP x IN array:
              PRINT x
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not if comparison with wrong type");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof TypeError);
            Assertions.assertEquals(2, e.getLinePosition());
            Assertions.assertEquals(10, e.getColumnPosition());
        }
    }

    @Test
    public void testOperationWithWrongType() {
        String input = """
            SET "hi" AS a
            DEFINE func WITH (x):
              IF (x > 0):
                PRINT "hi"
            func
              WITH x: a + 100
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow operation with wrong type");
        } catch (DSLException e) {
            System.out.println(e.getMessage());
            Assertions.assertTrue(e instanceof TypeError);
            Assertions.assertEquals(6, e.getLinePosition());
            Assertions.assertEquals(10, e.getColumnPosition());
        }
    }
}
