package e2e;

import core.Scope;
import core.evaluators.Evaluator;
import core.statements.Program;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

public class VideoExample {
    GifDSLCompiler compiler;
    @BeforeEach
    public void beforeEach() {
        compiler = new GifDSLCompiler();
    }

    @Test
    public void videoExample() {
        String input = """
            DEFINE RANDOM-COLOUR image WITH (min, max):
              RANDOM AS r
                WITH min: min
                WITH max: max
              RANDOM AS g
                WITH min: min
                WITH max: max
              RANDOM AS b
                WITH min: min
                WITH max: max
              CREATE-COLOUR AS colour
                WITH r: r
                WITH g: g
                WITH b: b
              COLOUR-FILL image AS image
                WITH colour: colour
              RETURN image
              
            SET 400 AS backgroundWidth
            SET 400 AS backgroundHeight
                        
            CREATE-RECTANGLE AS background
              WITH height: backgroundHeight
              WITH width: backgroundWidth
              WITH colour: #000000
                        
            LOAD "src/test/e2e/testImages/alex_summers.png" AS professor
            LOAD "src/test/e2e/testImages/snowy-background.jpeg" AS snowBackground
                        
            RESIZE snowBackground AS snowBackground
              WITH height: 400
              WITH width: 400
              
            SET 150 AS professorHeight
            SET 150 AS professorWidth
              
            RESIZE professor AS professor
              WITH height: professorHeight
              WITH width: professorWidth
              
            DEFINE WRITE-TITLE title:
              WRITE title AS titleElement
                WITH size: 20
                WITH font: "Sepia"
                WITH style: "Bold"
                WITH width: 400
                WITH height: 50
              
              RETURN titleElement
              
            WRITE-TITLE "Alex Summers" AS alexSummers
            WRITE-TITLE "Alex Winters" AS alexWinters
            WRITE-TITLE "Alex Disappears" AS alexDisappears
            WRITE-TITLE "Thanks for watching" AS thanks
                        
            CREATE-LIST AS frames
                        
            DEFINE OVERLAY-TEXT-IMAGE image WITH (text, background):
              OVERLAY image ON background AS imageBackground
                WITH x: 125
                WITH y: 150
              
              OVERLAY text ON imageBackground AS textBackground
                WITH x: 0
                WITH y: 100
              
              RETURN textBackground
                        
            LOOP i IN 1 TO 35:
              OVERLAY-TEXT-IMAGE professor AS regularProfessor
                WITH text: alexSummers
                WITH background: background
              
              ADD frames
                WITH item: regularProfessor
                        
            LOOP i in 1 TO 35:
              OVERLAY-TEXT-IMAGE professor AS winterProfessor
                WITH text: alexWinters
                WITH background: snowBackground
              
              ADD frames
                WITH item: winterProfessor
                        
            LOOP i in 1 TO 35:
              ROTATE professor AS rotatedProfessor
                WITH angle: i * 6
                
              SET i * 2 AS opacityAmount
              SET-OPACITY rotatedProfessor AS goneProfessor
                WITH amount: 100 - opacityAmount
              
              OVERLAY-TEXT-IMAGE goneProfessor AS disappearingProfessor
                WITH text: alexDisappears
                WITH background: background
              
              ADD frames
                WITH item: disappearingProfessor
                  
            SET backgroundWidth / 2 AS positionX
            SET backgroundHeight / 2 AS positionY
            RANDOM AS directionX
              WITH min: 1
              WITH max: 10
            SET 11 - directionX AS directionY
                        
            LOOP i IN 1 TO 40:
              SET positionX + directionX AS positionX
              SET positionY + directionY AS positionY
              IF (positionX < 0):
                SET 0 AS positionX
                SET directionX * -1 AS directionX
                RANDOM-COLOUR professor AS professor
                  WITH min: 0
                  WITH max: 200
              IF (positionX > backgroundWidth - professorWidth):
                SET backgroundWidth - professorWidth AS positionX
                SET directionX * -1 AS directionX
                RANDOM-COLOUR professor AS professor
                  WITH min: 0
                  WITH max: 200
              IF (positionY < 0):
                SET 0 AS positionY
                SET directionY * -1 AS directionY
                RANDOM-COLOUR professor AS professor
                  WITH min: 0
                  WITH max: 200
              IF (positionY > backgroundHeight - professorHeight):
                SET backgroundHeight - professorHeight AS positionY
                SET directionY * -1 AS directionY
                RANDOM-COLOUR professor AS professor
                  WITH min: 0
                  WITH max: 200
              OVERLAY professor ON background AS profMoving
                WITH x: positionX
                WITH y: positionY
              OVERLAY thanks ON profMoving AS frame
                WITH x: 0
                WITH y: 175
              ADD frames AS frames
                WITH item: frame
                
            SAVE frames
              WITH duration: 12
              WITH location: "video/videoExample.gif"
            """;
        Pair<Program, Scope> main = compiler.compile(CharStreams.fromString(input));
        Evaluator evaluator = new Evaluator();
        evaluator.visit(main.b, main.a);
    }
}
