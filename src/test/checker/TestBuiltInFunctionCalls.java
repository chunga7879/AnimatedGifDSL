package checker;

import builtin.functions.colour.CreateColour;
import com.sksamuel.scrimage.ImmutableImage;
import core.exceptions.DSLException;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.values.*;
import core.values.Image;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class TestBuiltInFunctionCalls extends TestStaticChecker {
    public void testFunctionCallHelper(String functionName, HashMap<String, Expression> args) {
        try {
            staticChecker.visit(scope, new FunctionCall(functionName, args, scope));
        } catch (DSLException e) {
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
            put("$target", new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetG() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetR() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallAdd() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("array", new Array());
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallColourFill() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
            put("colour", new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallCreateList() {
        HashMap<String, Expression> args = new HashMap<>();
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallCreateRectangle() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("width", new IntegerValue(1));
            put("height", new IntegerValue(2));
            put("colour", new Colour(1, 2, 3));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallCrop() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
            put("width", new IntegerValue(1));
            put("height", new IntegerValue(2));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallFilter() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
            put("filter", new StringValue("string"));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetHeight() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallGetWidth() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallLoad() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new StringValue("string"));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallOverlay() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
            put("on", new Image(img));
            put("x", new IntegerValue(1));
            put("y", new IntegerValue(1));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallPrint() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("msg", new StringValue("message"));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallRandom() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("min", new IntegerValue(1));
            put("max", new IntegerValue(2));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallResize() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
            put("width", new IntegerValue(1));
            put("height", new IntegerValue(2));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallRotate() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
            put("angle", new IntegerValue(1));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallSave() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("duration", new IntegerValue(1));
            put("location", new StringValue("string"));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallSetOpacity() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
            put("amount", new IntegerValue(1));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallTranslate() {
        ImmutableImage img = ImmutableImage.create(100, 100);
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new Image(img));
            put("x", new IntegerValue(1));
            put("y", new IntegerValue(2));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }

    @Test
    public void testFunctionCallWrite() {
        HashMap<String, Expression> args = new HashMap<>() {{
            put("$target", new StringValue("string"));
            put("width", new IntegerValue(1));
            put("height", new IntegerValue(1));
            put("font", new StringValue("string"));
            put("size", new IntegerValue(1));
            put("style", new StringValue("italic"));
        }};
        testFunctionCallHelper(CreateColour.ACTUAL_NAME, args);
    }
}
