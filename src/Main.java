import builtin.functions.*;
import builtin.functions.colour.CreateColour;
import builtin.functions.colour.GetB;
import builtin.functions.colour.GetG;
import builtin.functions.colour.GetR;
import core.Scope;
import core.evaluators.Evaluator;
import core.values.Colour;
import core.values.Function;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.misc.Pair;
import parser.GifDSLCompiler;

import java.io.IOException;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            boolean staticCheck = true;
            if (args.length < 1) {
                throw new IOException("Requires input file as arg");
            }
            if (args.length >= 2 && Objects.equals(args[1].toLowerCase(), "-nocheck")) {
                staticCheck = false;
            }
            GifDSLCompiler compiler = new GifDSLCompiler();
            compiler.setEnableStaticChecker(staticCheck);

            // Add built-in functions / constants
            compiler.addPredefinedValues(Set.ACTUAL_NAME, new Set());
            compiler.addPredefinedValues(CreateColour.ACTUAL_NAME, new CreateColour());
            compiler.addPredefinedValues(GetB.ACTUAL_NAME, new GetB());
            compiler.addPredefinedValues(GetG.ACTUAL_NAME, new GetG());
            compiler.addPredefinedValues(GetR.ACTUAL_NAME, new GetR());
            compiler.addPredefinedValues(Add.ACTUAL_NAME, new Add());
            compiler.addPredefinedValues(ColourFill.ACTUAL_NAME, new ColourFill());
            compiler.addPredefinedValues(CreateList.ACTUAL_NAME, new CreateList());
            compiler.addPredefinedValues(CreateRectangle.ACTUAL_NAME, new CreateRectangle());
            compiler.addPredefinedValues(Crop.ACTUAL_NAME, new Crop());
            compiler.addPredefinedValues(Filter.ACTUAL_NAME, new Filter());
            compiler.addPredefinedValues(GetHeight.ACTUAL_NAME, new GetHeight());
            compiler.addPredefinedValues(GetWidth.ACTUAL_NAME, new GetWidth());
            compiler.addPredefinedValues(Load.ACTUAL_NAME, new Load());
            compiler.addPredefinedValues(Overlay.ACTUAL_NAME, new Overlay());
            compiler.addPredefinedValues(Print.ACTUAL_NAME, new Print());
            compiler.addPredefinedValues(Random.ACTUAL_NAME, new Random());
            compiler.addPredefinedValues(Resize.ACTUAL_NAME, new Resize());
            compiler.addPredefinedValues(Rotate.ACTUAL_NAME, new Rotate());
            compiler.addPredefinedValues(Save.ACTUAL_NAME, new Save());
            compiler.addPredefinedValues(SetOpacity.ACTUAL_NAME, new SetOpacity());
            compiler.addPredefinedValues(Translate.ACTUAL_NAME, new Translate());
            compiler.addPredefinedValues(Write.ACTUAL_NAME, new Write());

            compiler.addPredefinedValues("white", new Colour(255, 255, 255));
            compiler.addPredefinedValues("black", new Colour(0, 0, 0));
            compiler.addPredefinedValues("red", new Colour(255, 0, 0));
            compiler.addPredefinedValues("green", new Colour(0, 255, 0));
            compiler.addPredefinedValues("blue", new Colour(0, 0, 255));

            Pair<Function, Scope> main = compiler.compile(CharStreams.fromFileName(args[0]));

            System.out.println("[Gif DSL Runner] Starting runner");
            Evaluator evaluator = new Evaluator();
            evaluator.visit(main.b.newChildScope(), main.a);
            System.out.println("[Gif DSL Runner] Finished runner");
        } catch (Exception e) {
            System.err.println(e.getMessage());
//            throw e;
        }
    }
}
