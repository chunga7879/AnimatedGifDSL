package e2e;

import core.Scope;
import core.evaluators.Evaluator;
import core.statements.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

public class UserStudyTwoTest {
    @Test
    public void testUserStudyProgram() {
        String input = """
            CREATE-RECTANGLE AS background
              WITH width: 3000
              WITH height: 1500
              WITH colour: black
            DEFINE MOVE-ROTATE image WITH (background, angle, x, y):
              ROTATE image
                WITH angle: angle
              OVERLAY image ON background
                WITH x: x
                WITH y: y
              RETURN background
            LOAD "src/test/e2e/userStudyImages/pumpkin.png" AS pumpkin
            CREATE-LIST AS frames
            SET 70 AS x
            SET 50 AS y
            LOOP i in 0 to 50:
              SET x + 3 as x
              SET y + 3 as y
              IF (x = 100):
                COLOUR-FILL pumpkin
                  WITH colour: white
              IF (y = 100):
                COLOUR-FILL pumpkin
                  WITH colour: white
              MOVE-ROTATE pumpkin AS frame
                WITH background: background
                WITH angle: i * 10
                WITH x: x
                WITH y: y
              ADD frames
                WITH item: frame
            SAVE frames
              WITH duration: 7
              WITH location: "src/test/e2e/userStudyImages/halloween.gif"
            """;
        GifDSLCompiler compiler = new GifDSLCompiler();
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
    }
}
