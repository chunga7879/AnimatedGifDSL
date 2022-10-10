package test.checker;

import core.Scope;
import core.checkers.StaticChecker;
import core.exceptions.DSLException;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.statements.FunctionDefinition;
import core.statements.Statement;
import core.values.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class TestStaticChecker {
    protected final String TRY_BLOCK_FAIL = "exception expected";
    protected final String CATCH_BLOCK_FAIL = "exception not expected";

    protected StaticChecker staticChecker;
    protected Scope scope;

    @BeforeEach
    public void runBefore() {
        scope = new Scope();
        staticChecker = new StaticChecker();
    }

    @Test
    public void testFunctionCallUserDefinedFunction() {
        List<Statement> statements = new ArrayList<>();
        try {
            staticChecker.visit(scope, new FunctionDefinition("foo", statements));
            staticChecker.visit(scope, new FunctionCall("foo", new HashMap<>(), scope));
        } catch (DSLException e) {
            System.out.println(e.message());
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionCallUndefinedFunction() {
        try {
            staticChecker.visit(scope, new FunctionCall("foo", new HashMap<>(), scope));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testFunctionDefinition() {
        List<Statement> statements = new ArrayList<>();
        try {
            staticChecker.visit(scope, new FunctionDefinition("foo", statements));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionDefinitionRedeclareUserDefinedFunction() {
        List<Statement> statements = new ArrayList<>();
        try {
            staticChecker.visit(scope, new FunctionDefinition("foo", statements));
            staticChecker.visit(scope, new FunctionDefinition("foo", statements));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testFunctionDefinitionRedeclareBuiltInFunction() {
        List<Statement> statements = new ArrayList<>();
        try {
            staticChecker.visit(scope, new FunctionDefinition("Add", statements));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testFunctionCallBuiltInFunctionWithMissingArguments() {
        try {
            Map<String, Value> args = new HashMap<>() {{
                put("min", new IntegerValue(1));
            }};
            staticChecker.visit(scope, new FunctionCall("Random", new HashMap<>(), scope));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testFunctionCallBuiltInFunctionWithIncorrectArgumentTypes() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("min", new StringValue("1"));
                put("max", new StringValue("2"));
            }};
            staticChecker.visit(scope, new FunctionCall("Random", args, scope));
            fail(CATCH_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testFunctionCallBuiltInFunctionWithTooManyArguments() {
        try {
            Map<String, Value> args = new HashMap<>() {{
                put("min", new IntegerValue(1));
                put("max", new IntegerValue(2));
                put("size", new IntegerValue(3));
            }};
            staticChecker.visit(scope, new FunctionCall("Random", new HashMap<>(), scope));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testFunctionCallBuiltInFunctionWithIncorrectArguments() {
        try {
            HashMap<String, Expression> args = new HashMap<>() {{
                put("foo", new IntegerValue(1));
                put("bar", new IntegerValue(2));
            }};
            staticChecker.visit(scope, new FunctionCall("Random", args, scope));
            fail(CATCH_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }
}
