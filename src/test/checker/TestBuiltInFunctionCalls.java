package test.checker;

import builtin.functions.*;
import builtin.functions.colour.CreateColour;
import builtin.functions.colour.GetB;
import builtin.functions.colour.GetG;
import builtin.functions.colour.GetR;
import com.sksamuel.scrimage.ImmutableImage;
import core.exceptions.DSLException;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.values.*;
import core.values.Image;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.fail;

public class TestBuiltInFunctionCalls extends TestStaticChecker {
    @Test
    public void testFunctionCallCreateColour() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("r", new IntegerValue(1));
                put("g", new IntegerValue(2));
                put("b", new IntegerValue(2));
            }};
            staticChecker.visit(scope, new FunctionCall(CreateColour.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallGetB() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("$target", new Colour(1, 2, 3));
            }};
            staticChecker.visit(scope, new FunctionCall(GetB.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallGetG() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("$target", new Colour(1, 2, 3));
            }};
            staticChecker.visit(scope, new FunctionCall(GetG.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallGetR() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("$target", new Colour(1, 2, 3));
            }};
            staticChecker.visit(scope, new FunctionCall(GetR.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallAdd() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("array", new Array());
            }};
            staticChecker.visit(scope, new FunctionCall(Add.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallCreateList() {
        try {
            HashMap<String, Expression> args = new HashMap<>();
            staticChecker.visit(scope, new FunctionCall(CreateList.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallFilter() {
        try {
            ImmutableImage img = ImmutableImage.create(100, 100);
            HashMap<String, Expression> args = new HashMap<>() {{
                put("$target", new Image(img));
                put("filter", new StringValue("string"));
            }};
            staticChecker.visit(scope, new FunctionCall(Filter.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            System.out.println(e.message());
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallPrint() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("msg", new StringValue("message"));
            }};
            staticChecker.visit(scope, new FunctionCall(Print.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallRandom() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("min", new IntegerValue(1));
                put("max", new IntegerValue(2));
            }};
            staticChecker.visit(scope, new FunctionCall(Random.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallSave() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("duration", new IntegerValue(1));
                put("location", new StringValue("string"));
            }};
            staticChecker.visit(scope, new FunctionCall(Save.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }
}
