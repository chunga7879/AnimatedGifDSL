package e2e;

import core.exceptions.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testDefineInNonBaseLevel() {
        List<String> inputs = new ArrayList<>() {{
            add("""
            DEFINE func:
              DEFINE func2:
                PRINT "hi"
            """);
            add("""
            IF (10 > 0):
              DEFINE func:
                PRINT "hi"
            """);
            add("""
            LOOP x IN 1 TO 2:
              DEFINE func:
                PRINT "hi"
            """);
        }};
        for (String input : inputs) {
            try {
                compiler.compile(CharStreams.fromString(input));
                Assertions.fail("Should not allow define inside other control statements");
            } catch (StatementException e) {
                System.out.println(e.getMessage());
                Assertions.assertEquals(2, e.getLinePosition());
            }
        }
    }

    @Test
    public void testReturnNotInDefine() {
        List<String> inputs = new ArrayList<>() {{
            add("""
            // empty
            PRINT "hi"
            RETURN 2
            """);
            add("""
            PRINT "hello world"
            IF (10 < 11):
              RETURN 3
            """);
            add("""
            SET 12 AS x
            IF (10 < x):
              RETURN 3
            """);
            add("""
            DEFINE func WITH (a):
              IF (10 > 0):
                RETURN a
            """);
            add("""
            DEFINE func WITH (b):
              LOOP i IN 10 TO 0:
                RETURN b
            """);
        }};
        for (String input : inputs) {
            try {
                compiler.compile(CharStreams.fromString(input));
                Assertions.fail("Should not allow define inside other control statements");
            } catch (StatementException e) {
                System.out.println(e.getMessage());
                Assertions.assertEquals(3, e.getLinePosition());
            }
        }
    }

    @Test
    public void testUsingUserDefinedFunctionReturn() {
        String input = """
            DEFINE func WITH (x):
              RETURN x
            func as a
              WITH x: "hi"
            PRINT a
            """;
        compiler.compile(CharStreams.fromString(input));
    }

    @Test
    public void testUsingUserDefinedFunctionReturnAsWrongType() {
        String input = """
            DEFINE func WITH (x):
              SET x AS i
              RETURN i
            func as a
              WITH x: "hi"
            func as b
              WITH x: 100
            PRINT a
            PRINT b
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow operation with wrong type");
        } catch (FunctionException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(9, e.getLinePosition());
        }
    }

    @Test
    public void testNoReturnFunctions() {
        String inputPrint = """
            SET "hello, world" as a
            PRINT "hi"
            PRINT a
            PRINT "hi" as b
            """;
        String inputSave = """
            CREATE-RECTANGLE as image
              WITH width: 100
              WITH height: 100
              WITH colour: #FFFFFF
            CREATE-LIST AS frames
            ADD frames
              WITH item: image
            SAVE frames as this
              WITH duration: 10
              WITH location: "./test.gif"
            """;
        String inputUserDefined = """
            SET 2 as var
            DEFINE func a:
              SET 10 AS a
            func var
            func var AS var
            """;
        List<Pair<String, Integer>> inputs = new ArrayList<>() {{
            add(new Pair<>(inputPrint, 4));
            add(new Pair<>(inputSave, 8));
            add(new Pair<>(inputUserDefined, 5));
        }};
        for (Pair<String, Integer> input : inputs) {
            try {
                compiler.compile(CharStreams.fromString(input.a));
                Assertions.fail("Should not allow 'as' for functions with no return");
            } catch (FunctionException e) {
                System.out.println(e.getMessage());
                Assertions.assertEquals(input.b, e.getLinePosition());
            }
        }
    }

    @Test
    public void testStatementsAfterReturn() {
        String input = """
            DEFINE func WITH (x):
              SET x + 5 AS x
              RETURN x
              SET 10 AS x
            func as a
              WITH x: 10
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow statements after return");
        } catch (StatementException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(4, e.getLinePosition());
        }
    }

    @Test
    public void testFunctionOverlapsVariable() {
        String input = """
            SET 0 as func
            DEFINE func:
              SET 10 AS x
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow functions to be named the same as variables");
        } catch (FunctionNameException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(2, e.getLinePosition());
        }
    }

    @Test
    public void testVariableOverlapsFunction() {
        String input = """
            DEFINE func:
              SET 10 AS x
            SET 0 as func
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow variables to be named the same as functions");
        } catch (FunctionNameException e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(3, e.getLinePosition());
        }
    }

    @Test
    public void testParameterOverlapsFunction() {
        String input = """
            DEFINE func WITH (x):
              RETURN x
            DEFINE func2 WITH (func):
              func
                WITH x: 10
            func2
              WITH func: 10
            """;
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow function name that is overlapped to be used as a function");
        } catch (TypeError e) {
            System.out.println(e.getMessage());
            Assertions.assertEquals(4, e.getLinePosition());
            Assertions.assertEquals(2, e.getColumnPosition());
        }
    }

    @Test
    public void testVariablesAndFunctionsOverlapConstant() {
        String inputVariable = """
            SET #FFFFFF as white
            """;
        String inputFunction = """
            DEFINE black WITH (x):
              RETURN x
            """;
        String inputFunctionTarget = """
            DEFINE func black:
              SET 0 AS black
              RETURN black
            """;
        String inputFunctionParameter = """
            DEFINE fun WITH (blue):
              RETURN blue
            """;
        String inputLoop = """
            LOOP black IN 1 TO 10:
              SET black AS x
            """;
        List<String> inputs = new ArrayList<>() {{
            add(inputVariable);
            add(inputFunction);
            add(inputFunctionTarget);
            add(inputFunctionParameter);
            add(inputLoop);
        }};
        for (String input : inputs) {
            try {
                compiler.compile(CharStreams.fromString(input));
                Assertions.fail("Should not allow variables, functions, and function parameters to use constant names");
            } catch (FunctionException e) {
                System.out.println(e.getMessage());
                Assertions.assertEquals(1, e.getLinePosition());
            }
        }
    }
}
