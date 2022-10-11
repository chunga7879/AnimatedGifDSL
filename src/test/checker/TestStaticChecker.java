package checker;

import builtin.functions.Add;
import builtin.functions.Random;
import core.Scope;
import core.checkers.StaticChecker;
import core.exceptions.DSLException;
import core.expressions.ArithmeticExpression;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.expressions.VariableExpression;
import core.expressions.arithmetic.AdditionVisitor;
import core.statements.FunctionDefinition;
import core.statements.Statement;
import core.statements.VariableAssignment;
import core.values.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class TestStaticChecker {
    private static final String TRY_BLOCK_FAIL = "exception expected";
    private static final String CATCH_BLOCK_FAIL = "exception not expected";

    private StaticChecker staticChecker;
    private Scope scope;

    @BeforeEach
    public void runBefore() {
        scope = new Scope();
        staticChecker = new StaticChecker();
    }

    @Test
    public void testFunctionCallUserDefinedFunction() {
        List<Statement> statements = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        try {
            staticChecker.visit(scope, new FunctionDefinition("foo", statements, params));
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
        HashMap<String, String> params = new HashMap<>();
        try {
            staticChecker.visit(scope, new FunctionDefinition("foo", statements, params));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testFunctionDefinitionRedeclareUserDefinedFunction() {
        List<Statement> statements = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        try {
            staticChecker.visit(scope, new FunctionDefinition("foo", statements, params));
            staticChecker.visit(scope, new FunctionDefinition("foo", statements, params));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testFunctionDefinitionRedeclareBuiltInFunction() {
        List<Statement> statements = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        try {
            staticChecker.visit(scope, new FunctionDefinition(Add.ACTUAL_NAME, statements, params));
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
            staticChecker.visit(scope, new FunctionCall(Random.ACTUAL_NAME, new HashMap<>(), scope));
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
            staticChecker.visit(scope, new FunctionCall(Random.ACTUAL_NAME, args, scope));
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
            staticChecker.visit(scope, new FunctionCall(Random.ACTUAL_NAME, new HashMap<>(), scope));
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
            staticChecker.visit(scope, new FunctionCall(Random.ACTUAL_NAME, args, scope));
            fail(CATCH_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testFunctionCallBuiltInFunctionWithExpressionArgument() {
        try {
            Expression a = new IntegerValue(1);
            Expression b = new IntegerValue(1);
            HashMap<String, Expression> args = new HashMap<>() {{
                put("min", new IntegerValue(1));
                put("max", new ArithmeticExpression(a, b, new AdditionVisitor()));
            }};
            staticChecker.visit(scope, new FunctionCall(Random.ACTUAL_NAME, args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testValidArithmeticExpression() {
        Expression a = new IntegerValue(1);
        Expression b = new IntegerValue(2);
        try {
            staticChecker.visit(scope, new ArithmeticExpression(a, b, new AdditionVisitor()));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testInvalidArithmeticExpression() {
        Expression a = new IntegerValue(1);
        Expression b = new StringValue("2");
        try {
            staticChecker.visit(scope, new ArithmeticExpression(a, b, new AdditionVisitor()));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }

    @Test
    public void testValidFunctionCallUserDefinedFunction() {
        List<Statement> statements = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>() {{
            put("num", IntegerValue.NAME);
        }};
        HashMap<String, Expression> args = new HashMap<>() {{
            put("num", new IntegerValue(1));
        }};
        try {
            staticChecker.visit(scope, new FunctionDefinition("foo", statements, params));
            staticChecker.visit(scope, new FunctionCall("foo", args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testInvalidFunctionCallUserDefinedFunction() {
        List<Statement> statements = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>() {{
            put("num", IntegerValue.NAME);
        }};
        HashMap<String, Expression> args = new HashMap<>() {{
            put("num", new StringValue(""));
        }};
        try {
            staticChecker.visit(scope, new FunctionDefinition("foo", statements, params));
            staticChecker.visit(scope, new FunctionCall("foo", args, scope));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    public void testDefinedVariable() {
        try {
            staticChecker.visit(scope, new VariableAssignment("test", new IntegerValue(1)));
            staticChecker.visit(scope, new VariableExpression("test"));
        } catch (DSLException e) {
            fail(CATCH_BLOCK_FAIL);
        }
    }

    @Test
    public void testUndefinedVariable() {
        try {
            staticChecker.visit(scope, new VariableExpression("test"));
            fail(TRY_BLOCK_FAIL);
        } catch (DSLException e) {
            // expected
        }
    }
}
