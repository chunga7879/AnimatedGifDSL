package e2e;

import core.exceptions.FunctionException;
import core.statements.ExpressionWrapper;
import core.statements.Program;
import core.statements.Statement;
import core.statements.VariableAssignment;
import org.antlr.v4.runtime.CharStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

import java.util.ArrayList;
import java.util.List;

public class End2EndShortcutProcessorTest {
    private GifDSLCompiler compiler;

    @BeforeEach
    public void runBefore() {
        compiler = new GifDSLCompiler();
    }

    @Test
    public void testNoShortcutForNoReturn() {
        String input = """
            SET 0 as x
            SET x
            
            SET "hi" as text
            PRINT text
            
            CREATE-LIST as frames
            SAVE frames
              WITH duration: 10
              WITH location: "./gif.gif"
            """;
        compiler.setEnableShortcuts(true);
        Program p = compiler.compile(CharStreams.fromString(input)).a;
        Assertions.assertEquals(6, p.statements().size());
        Assertions.assertTrue(p.statements().get(0) instanceof VariableAssignment);
        Assertions.assertTrue(p.statements().get(1) instanceof ExpressionWrapper);
        Assertions.assertTrue(p.statements().get(2) instanceof VariableAssignment);
        Assertions.assertTrue(p.statements().get(3) instanceof ExpressionWrapper);
        Assertions.assertTrue(p.statements().get(4) instanceof VariableAssignment);
        Assertions.assertTrue(p.statements().get(5) instanceof ExpressionWrapper);
    }

    @Test
    public void testNoShortcutForNoTarget() {
        String createListInput = """
            SET 0 as x
            CREATE-LIST x
            """;
        String createRectangleInput = """
            SET 0 as x
            CREATE-COLOUR x
              WITH r: 0
              WITH g: 0
              WITH b: 0
            """;
        String createColourInput = """
            SET 0 as x
            CREATE-RECTANGLE x
              WITH width: 10
              WITH height: 10
              WITH colour: white
            """;
        String randomInput = """
            SET 0 as x
            RANDOM x
              WITH min: 10
              WITH max: 10
            """;
        List<String> inputs = new ArrayList<>() {{
            add(createListInput);
            add(createRectangleInput);
            add(createColourInput);
            add(randomInput);
        }};
        for (String input : inputs) {
            try {
                compiler.setEnableShortcuts(true);
                compiler.compile(CharStreams.fromString(input));
                Assertions.fail("Should not allow 'as' for functions with no return");
            } catch (FunctionException e) {
                System.out.println(e.getMessage());
                Assertions.assertEquals(2, e.getLinePosition());
            }
        }
    }

    @Test
    public void testNoShortcutForDifferentTargetReturnType() {
        String input = """
            SET "text" AS text
            SET #FF1122 AS colour
            CREATE-RECTANGLE as img
              WITH width: 10
              WITH height: 10
              WITH colour: white
            GET-R colour
            GET-G colour
            GET-B colour
            GET-HEIGHT img
            GET-WIDTH img
            LOAD text
            WRITE text
              WITH width: 10
              WITH height: 10
              WITH size: 10
              WITH font: "Arial"
              WITH style: "italic"
            """;
        compiler.setEnableShortcuts(true);
        Program p = compiler.compile(CharStreams.fromString(input)).a;
        Assertions.assertEquals(10, p.statements().size());
        for (int i = 0; i < p.statements().size(); i++) {
            if (i < 3) {
                Assertions.assertTrue(p.statements().get(i) instanceof VariableAssignment);
            } else {
                Assertions.assertTrue(p.statements().get(i) instanceof ExpressionWrapper);
            }
        }
    }

    @Test
    public void testShortcutForReturnTarget() {
        String input = """
            CREATE-LIST as list
            CREATE-RECTANGLE as img
              WITH width: 10
              WITH height: 10
              WITH colour: white
            CREATE-RECTANGLE as background
              WITH width: 20
              WITH height: 20
              WITH colour: black
            ADD list
              WITH item: 0
            COLOUR-FILL img
              WITH colour: black
            CROP img
              WITH width: 10
              WITH height: 10
            FILTER img
              WITH filtering: "greyscale"
            OVERLAY img ON background
              WITH x: 0
              WITH y: 0
            RESIZE img
              WITH width: 20
              WITH height: 20
            ROTATE img
              WITH angle: 20
            SET-OPACITY img
              WITH amount: 20
            TRANSLATE img
              WITH x: 20
              WITH y: 20
            """;
        compiler.setEnableShortcuts(true);
        Program p = compiler.compile(CharStreams.fromString(input)).a;
        Assertions.assertEquals(12, p.statements().size());
        for (Statement statement : p.statements()) {
            Assertions.assertTrue(statement instanceof VariableAssignment);
        }
    }

    @Test
    public void testShortcutForReturnOn() {
        String input = """
            CREATE-RECTANGLE as img
              WITH width: 10
              WITH height: 10
              WITH colour: white
            CREATE-RECTANGLE as background
              WITH width: 20
              WITH height: 20
              WITH colour: black
            OVERLAY img ON background
              WITH x: 0
              WITH y: 0
            """;
        compiler.setEnableShortcuts(true);
        Program p = compiler.compile(CharStreams.fromString(input)).a;
        Assertions.assertEquals(3, p.statements().size());
        Statement statement = p.statements().get(2);
        Assertions.assertTrue(statement instanceof VariableAssignment);
        Assertions.assertEquals("background", ((VariableAssignment) statement).dest());
    }
}
