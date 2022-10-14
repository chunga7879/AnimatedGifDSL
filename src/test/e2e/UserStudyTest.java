package e2e;

import builtin.functions.*;
import builtin.functions.colour.CreateColour;
import core.Scope;
import core.evaluators.Evaluator;
import core.statements.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

public class UserStudyTest {
    @Test
    public void testUserStudy() {
        String input = """
            CREATE-COLOUR AS black
              WITH r: 0
              WITH g: 0
              WITH b: 0
            CREATE-RECTANGLE AS background
              WITH width: 3000
              WITH height: 1500
              WITH colour: black
            LOAD "./pumpkin.png" AS pumpkin
            CREATE-LIST AS frames
            SET 50 AS x
            SET 50 AS y
            LOOP i in (0, 50):
              SET x + 3 as x
              SET y + 3 as y
              OVERLAY pumpkin ON background AS frame
                WITH x: x
                WITH y: y
              ADD frames
                WITH item: frame
            SAVE frames
              WITH duration: 3
              WITH location: "./halloweenTest.gif"
            """;
        GifDSLCompiler compiler = new GifDSLCompiler();
        compiler.addPredefinedValues("create-rectangle", new CreateRectangle());
        compiler.addPredefinedValues("create-colour", new CreateColour());
        compiler.addPredefinedValues("overlay", new Overlay());
        compiler.addPredefinedValues("load", new Load());
        compiler.addPredefinedValues("create-list", new CreateList());
        compiler.addPredefinedValues("set", new Set());
        compiler.addPredefinedValues("colour-fill", new ColourFill());
        compiler.addPredefinedValues("add", new Add());
        compiler.addPredefinedValues("save", new Save());

        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
    }
}
