package checker;

import builtin.functions.*;
import builtin.functions.colour.CreateColour;
import builtin.functions.colour.GetB;
import builtin.functions.colour.GetG;
import builtin.functions.colour.GetR;
import com.sksamuel.scrimage.ImmutableImage;
import core.Scope;
import core.checkers.StaticChecker;
import core.exceptions.DSLException;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.values.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import parser.GifDSLCompiler;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class TestBuiltInFunctionCalls {
    private static final String CATCH_BLOCK_FAIL = "exception not expected";

    private StaticChecker staticChecker;
    private Scope scope;

    @BeforeEach
    public void runBefore() {
        GifDSLCompiler compiler = new GifDSLCompiler();
        scope = compiler.getRootScope();
        staticChecker = compiler.createStaticChecker();
    }

    public void testFunctionCallHelper(String functionName, HashMap<String, Expression> args) {
        try {
            staticChecker.visit(scope, new FunctionCall(functionName.toLowerCase(), args));
        } catch (DSLException e) {
            System.out.println(e);
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallCreateColour() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("r", new IntegerValue(1));
            put("g", new IntegerValue(2));
            put("b", new IntegerValue(2));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetB() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(GetB.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetG() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(GetG.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetR() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(GetR.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallAdd() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Array());
            put("item", new IntegerValue(1));
        }};
        testFunctionCallHelper(Add.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallColourFill() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
            put("colour", new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(ColourFill.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallCreateList() {
        HashMap<String, Expression> args = new HashMap<>();
        testFunctionCallHelper(CreateList.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallCreateRectangle() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("width", new IntegerValue(1));
            put("height", new IntegerValue(2));
            put("colour", new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(CreateRectangle.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallCrop() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
            put("width", new IntegerValue(1));
            put("height", new IntegerValue(2));
        }};
        testFunctionCallHelper(Crop.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallFilter() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
            put("filtering", new StringValue("string"));
        }};
        testFunctionCallHelper(Filter.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetHeight() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
        }};
        testFunctionCallHelper(GetHeight.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetWidth() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
        }};
        testFunctionCallHelper(GetWidth.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallLoad() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new StringValue("string"));
        }};
        testFunctionCallHelper(Load.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallOverlay() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
            put(AbstractFunction.PARAM_ON, new Image(img));
            put("x", new IntegerValue(1));
            put("y", new IntegerValue(1));
        }};
        testFunctionCallHelper(Overlay.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallPrint() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new StringValue("string"));
        }};
        testFunctionCallHelper(Print.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallRandom() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("min", new IntegerValue(1));
            put("max", new IntegerValue(2));
        }};
        testFunctionCallHelper(Random.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallResize() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
            put("width", new IntegerValue(1));
            put("height", new IntegerValue(2));
        }};
        testFunctionCallHelper(Resize.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallRotate() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
            put("angle", new IntegerValue(1));
        }};
        testFunctionCallHelper(Rotate.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallSave() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Array());
            put("duration", new IntegerValue(1));
            put("location", new StringValue("string"));
        }};
        testFunctionCallHelper(Save.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallSetOpacity() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
            put("amount", new IntegerValue(1));
        }};
        testFunctionCallHelper(SetOpacity.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallTranslate() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new Image(img));
            put("x", new IntegerValue(1));
            put("y", new IntegerValue(2));
        }};
        testFunctionCallHelper(Translate.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallWrite() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put(AbstractFunction.PARAM_TARGET, new StringValue("string"));
            put("width", new IntegerValue(1));
            put("height", new IntegerValue(1));
            put("font", new StringValue("string"));
            put("size", new IntegerValue(1));
            put("style", new StringValue("italic"));
        }};
        testFunctionCallHelper(Write.ACTUAL_NAME, args);
    }
}
