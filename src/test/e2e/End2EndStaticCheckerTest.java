package e2e;

import core.exceptions.FunctionException;
import core.exceptions.FunctionNameException;
import core.exceptions.NameError;
import core.exceptions.TypeError;
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
        } catch (NameError e) {
            System.out.println(e.getMessage());
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
        } catch (FunctionNameException e) {
            System.out.println(e.getMessage());
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
        } catch (FunctionNameException e) {
            System.out.println(e.getMessage());
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
        } catch (FunctionException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(3, e.getLinePosition());
            Assertions.assertEquals(0, e.getColumnPosition());
        }
    }

    @Test
    public void testUserDefinedMissingParameterButHasVariable() {
        String input = """
            DEFINE FUNC WITH (a, b):
              RETURN a + b
            SET 0 AS a
            SET 100 AS b
            FUNC AS var
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow missing parameters");
        } catch (FunctionException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(5, e.getLinePosition());
            Assertions.assertEquals(0, e.getColumnPosition());
        }
    }

    @Test
    public void testUserDefinedMissingTargetButHasVariable() {
        String input = """
            SET 0 AS a
            DEFINE FUNC a:
              RETURN a
            FUNC AS var
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow missing target");
        } catch (FunctionException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(4, e.getLinePosition());
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
        } catch (FunctionException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(3, e.getLinePosition());
            Assertions.assertEquals(0, e.getColumnPosition());
        }
    }

    @Test
    public void testUserDefinedWrongParameterNameButHasVariable() {
        String input = """
            DEFINE FUNC WITH (a, b):
              RETURN a + b
            SET 0 AS a
            SET 100 AS b
            FUNC AS var
              WITH a: 10
              WITH c: 20
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow wrong parameters");
        } catch (FunctionException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(5, e.getLinePosition());
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
        } catch (FunctionException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(5, e.getLinePosition());
            Assertions.assertEquals(4, e.getColumnPosition());
        }
    }

    @Test
    public void testUserDefinedArgumentType() {
        // User defined functions do not have a defined type,
        // the error will be inside the function if it called with a bad type
        String input = """
            DEFINE FUNC WITH (a):
              PRINT a
            FUNC
              WITH a: 10
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow wrong parameter type");
        } catch (FunctionException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(2, e.getLinePosition());
            Assertions.assertEquals(2, e.getColumnPosition());
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
        } catch (TypeError e) {
            System.out.println(e.getMessage());
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
        } catch (TypeError e) {
            System.out.println(e.getMessage());
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
        } catch (TypeError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(6, e.getLinePosition());
            Assertions.assertEquals(10, e.getColumnPosition());
        }
    }

    @Test
    public void testUserDefinedFunctionNotCalled() {
        String badInput = """
            DEFINE FUNC WITH (a):
              RETURN a + b
            """;
        try {
            compiler.compile(CharStreams.fromString(badInput));
            Assertions.fail("Should not allow missing parameters");
        } catch (NameError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(2, e.getLinePosition());
            Assertions.assertEquals(13, e.getColumnPosition());
        }

        String goodInput = """
            DEFINE FUNC WITH (a, b):
              RETURN a + b
            """;
        compiler.compile(CharStreams.fromString(goodInput));
    }
}
