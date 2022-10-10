package test.checker;

import builtin.functions.Random;
import core.Scope;
import core.evaluators.StaticChecker;
import core.exceptions.DSLException;
import core.expressions.FunctionCall;
import core.statements.FunctionDefinition;
import core.statements.Return;
import core.statements.Statement;
import core.values.Array;
import core.values.Function;
import core.values.IntegerValue;
import core.values.StringValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class TestStaticChecker {
    private final String TRY_BLOCK_FAIL = "exception expected";
    private final String CATCH_BLOCK_FAIL = "exception not expected";

    private StaticChecker staticChecker;
    private Scope scope;

    @BeforeEach
    public void runBefore() {
        staticChecker = new StaticChecker();
        scope = new Scope();
    }

    /* FunctionCall */
    @Test
    public void testCallUndefinedFunction() {
        try {
            staticChecker.visit(scope, new FunctionCall("foo", new HashMap<>(), scope));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testCallBuiltInFunction() {
        try {
            staticChecker.visit(scope, new FunctionCall("ADD", new HashMap<>(), scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testCallUserDefinedFunction() {
        try {
            staticChecker.visit(scope, new FunctionCall("foo", new HashMap<>(), scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    /* FunctionDefinition */
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
        scope.setVar("foo", new Function(statements));
        try {
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
            staticChecker.visit(scope, new FunctionDefinition("ADD", statements));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testCallAdd() {

    }

    @Test
    public void testCallSave() {

    }

    @Test
    public void testCallLoad() {

    }

    @Test
    public void testCallPrint() {

    }

    // function call too many arguments
    // function call not enough arguments
    // function call incorrect type
    // function missing argument(s)

//    @Test
//    public void testAbstractFunction() {
//        staticChecker.visit(scope, new FunctionCall());
//    }

    @Test
    public void testCallRandomValid() {
        scope.setVar("min", new IntegerValue(1));
        scope.setVar("max", new IntegerValue(1));
        scope.setVar("size", new IntegerValue(1));
        try {
            staticChecker.visit(scope, new Random());
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testCallRandomInvalid() {
        scope.setVar("min", new StringValue("test"));
        try {
            staticChecker.visit(scope, new Random());
        } catch (DSLException e) {
            // expected
        }
    }
}
