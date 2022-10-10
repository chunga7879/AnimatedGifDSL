package test.checker;

import core.exceptions.DSLException;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.values.Array;
import core.values.Colour;
import core.values.IntegerValue;
import core.values.StringValue;
import org.junit.jupiter.api.Test;

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
            staticChecker.visit(scope, new FunctionCall("Create-Colour", args, scope));
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
            staticChecker.visit(scope, new FunctionCall("Get-B", args, scope));
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
            staticChecker.visit(scope, new FunctionCall("Get-G", args, scope));
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
            staticChecker.visit(scope, new FunctionCall("Get-R", args, scope));
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
            staticChecker.visit(scope, new FunctionCall("Add", args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallCreateList() {
        try {
            HashMap<String, Expression> args = new HashMap<>();
            staticChecker.visit(scope, new FunctionCall("Create-List", args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallPrint() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("msg", new StringValue("message"));
            }};
            staticChecker.visit(scope, new FunctionCall("Print", args, scope));
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
            staticChecker.visit(scope, new FunctionCall("Random", args, scope));
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
            staticChecker.visit(scope, new FunctionCall("Save", args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }
}
