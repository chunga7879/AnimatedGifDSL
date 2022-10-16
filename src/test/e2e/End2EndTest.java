package e2e;

import core.Scope;
import core.evaluators.Evaluator;
import core.exceptions.DSLException;
import core.exceptions.NameError;
import core.statements.Program;
import core.values.Value;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

import java.util.List;

public class End2EndTest {
    private GifDSLCompiler compiler;
    private Evaluator evaluator;

    @BeforeEach
    public void runBefore() {
        compiler = new GifDSLCompiler();
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
    public void testSaveProgram() {
        String input = """
            CREATE-RECTANGLE AS background
              WITH width: 400
              WITH height: 400
              WITH colour: #82A24F
                        
            LOAD "src/test/e2e/testImages/cat.png" AS cat
            
            RESIZE cat AS smallerCat
              WITH width: 20
              WITH height: 20
            
            CREATE-LIST AS frames
            
            OVERLAY smallerCat ON background AS frame
              WITH x: 200
              WITH y: 200
            ADD frames
              WITH item: frame
                        
            LOOP i in 1 to 10:
              ROTATE smallerCat AS rotatedCat
                WITH angle: i
              // ADD frames
                // WITH item: rotatedCat
              OVERLAY rotatedCat ON background AS frame
                WITH x: 200
                WITH y: 200
              ADD frames
                WITH item: frame
                        
            COLOUR-FILL smallerCat AS coloured-cat
              WITH colour: #123456
              
            ADD frames
              WITH item: coloured-cat
                                                
            SAVE frames
              WITH duration: 5
              WITH location: "src/test/e2e/testImages/newGif.gif"
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b.newChildScope(), main.a);
    }

    @Test
    public void testRectangleProgram() {
        String input = """     
            LOAD "src/test/e2e/testImages/cat.png" AS cat
            
            RESIZE cat AS smallerCat
              WITH width: 100
              WITH height: 100
            
            CREATE-LIST AS frames
                        
            CREATE-RECTANGLE AS background
              WITH width: 400
              WITH height: 400
              WITH colour: #82A24F
            ADD frames
                WITH item: background
                
            CREATE-RECTANGLE AS background2
              WITH width: 400
              WITH height: 400
              WITH colour: #000000
            ADD frames
              WITH item: background2
                        
            COLOUR-FILL smallerCat AS coloured-cat
              WITH colour: #FE892D
              
            ADD frames
              WITH item: coloured-cat
                                                
            SAVE frames
              WITH duration: 5
              WITH location: "src/test/e2e/testImages/simpleNewGif.gif"
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b.newChildScope(), main.a);
    }

    @Test
    public void testUserTesting2() {
        String input = """     
            LOAD "src/test/e2e/testImages/cat.png" AS cat
                        
            RESIZE cat AS smallerCat
              WITH height: 40
              WITH width: 40
                        
            CREATE-LIST AS frames
                        
            SET 200 AS y
            SET 200 AS x
                        
            CREATE-RECTANGLE AS background
              WITH width: 400
              WITH height: 400
              WITH colour: #001339
                                
            DEFINE JUMP image WITH (background):
              LOOP i IN 1 TO 3:
                SET y + 1 AS y
                OVERLAY image ON background AS frame
                  WITH x: 200
                  WITH y: y
                ADD frames
                  WITH item: frame
                        
              LOOP i IN 1 TO 3:
                SET y - 1 AS y
                OVERLAY image ON background AS frame
                  WITH x: 200
                  WITH y: y
                ADD frames
                  WITH item: frame
                        
            LOOP i IN 1 TO 10:
              LOOP i IN 1 TO 3:
                SET y + 1 AS y
                OVERLAY smallerCat ON background AS frame
                  WITH x: 200
                  WITH y: y
                ADD frames
                  WITH item: frame
                        
              LOOP i IN 1 TO 3:
                SET y - 1 AS y
                OVERLAY smallerCat ON background AS frame
                  WITH x: 200
                  WITH y: y
                ADD frames
                  WITH item: frame
              COLOUR-FILL smallerCat AS colourcat
                WITH colour: #7C2BCE
              ROTATE colourcat AS rotatedCat
                WITH angle: 180
              OVERLAY rotatedCat ON background AS frame
                WITH x: 200
                WITH y: y
              ADD frames
                WITH item: frame
                        
            SAVE frames
              WITH duration: 7
              WITH location: "src/test/e2e/testImages/final.gif"
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b.newChildScope(), main.a);
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
        try {
            compiler.compile(CharStreams.fromString(input));
            Assertions.fail("Should not allow editing of constant");
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testLoopUserDefinedFunctionParameter() {
        String input = """
            CREATE-LIST as list1
            DEFINE func WITH (texts):
              LOOP text IN texts:
                ADD list1
                  WITH item: text
            CREATE-LIST as list2
            ADD list2
              WITH item: "hi1"
            ADD list2
              WITH item: "hi2"
            func
              WITH texts: list2
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("list1"));
        Assertions.assertTrue(main.b.hasVar("list2"));
        List<Value> list1 = main.b.getVar("list1").asArray().get();
        List<Value> list2 = main.b.getVar("list2").asArray().get();
        Assertions.assertEquals(2, list1.size());
        Assertions.assertEquals(2, list2.size());
        Assertions.assertEquals("hi1", list1.get(0).asString().get());
        Assertions.assertEquals("hi2", list1.get(1).asString().get());
        Assertions.assertEquals("hi1", list2.get(0).asString().get());
        Assertions.assertEquals("hi2", list2.get(1).asString().get());
    }

    @Test
    public void testParameterOverlapsFunction() {
        String input = """
            SET 2 as a
            SET 3 as b
            SET 4 as c
            DEFINE func:
              SET 10 / a AS a
            DEFINE func2 WITH (func):
              SET func AS b
            DEFINE func3 func2:
              SET func2 * c as c
            func
            func2
              WITH func: 10
            func3 20
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("a"));
        Assertions.assertTrue(main.b.hasVar("b"));
        Assertions.assertTrue(main.b.hasVar("c"));
        Assertions.assertTrue(main.b.hasVar("func"));
        Assertions.assertTrue(main.b.hasVar("func2"));
        Assertions.assertTrue(main.b.hasVar("func3"));
        Assertions.assertEquals(5, main.b.getVar("a").asInteger().get());
        Assertions.assertEquals(10, main.b.getVar("b").asInteger().get());
        Assertions.assertEquals(80, main.b.getVar("c").asInteger().get());
        Assertions.assertEquals(0, main.b.getVar("func").asFunction().getParams().size());
        Assertions.assertEquals(1, main.b.getVar("func2").asFunction().getParams().size());
        Assertions.assertEquals(1, main.b.getVar("func3").asFunction().getParams().size());
    }

    @Test
    public void testFunctionAsParameters() {
        String input = """
            DEFINE add-numbers a WITH (b):
              RETURN a + b
            DEFINE subtract-numbers a WITH (b):
              RETURN a - b
            DEFINE multiply-numbers a WITH (b):
              RETURN a * b
            DEFINE combineFunc func1 WITH (func2, a, b):
              func1 a as ret1
                WITH b: a
              func1 b as ret2
                WITH b: b
              func2 ret1 as ret
                WITH b: ret2
              RETURN ret
            combineFunc add-numbers as x
              WITH func2: subtract-numbers
              WITH a: 10
              WITH b: 8
            combineFunc multiply-numbers as y
              WITH func2: subtract-numbers
              WITH a: -13
              WITH b: 50
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
        Assertions.assertTrue(main.b.hasVar("x"));
        Assertions.assertTrue(main.b.hasVar("y"));
        Assertions.assertTrue(main.b.hasVar("add-numbers"));
        Assertions.assertTrue(main.b.hasVar("subtract-numbers"));
        Assertions.assertTrue(main.b.hasVar("multiply-numbers"));
        Assertions.assertTrue(main.b.hasVar("combinefunc"));
        Assertions.assertEquals(4, main.b.getVar("x").asInteger().get());
        Assertions.assertEquals(-2331, main.b.getVar("y").asInteger().get());
        Assertions.assertEquals(2, main.b.getVar("add-numbers").asFunction().getParams().size());
        Assertions.assertEquals(2, main.b.getVar("subtract-numbers").asFunction().getParams().size());
        Assertions.assertEquals(2, main.b.getVar("multiply-numbers").asFunction().getParams().size());
        Assertions.assertEquals(4, main.b.getVar("combinefunc").asFunction().getParams().size());
    }
}
