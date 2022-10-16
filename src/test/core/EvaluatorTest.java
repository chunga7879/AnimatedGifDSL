package core;

import core.evaluators.Evaluator;
import core.expressions.ArithmeticExpression;
import core.expressions.ComparisonExpression;
import core.expressions.FunctionCall;
import core.expressions.VariableExpression;
import core.expressions.arithmetic.AdditionVisitor;
import core.expressions.comparison.GTVisitor;
import core.expressions.comparison.LTVisitor;
import core.statements.*;
import core.values.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EvaluatorTest {

    @Test
    public void testVariableGetSet() {
        final String testVar = "testvar";
        final String testVar2 = "testvar2";
        Scope s = new Scope();
        IntegerValue testInt = new IntegerValue(1);

        s.setVar(testVar, testInt);

        HashMap<String, String> params = new HashMap<>();
        Function testFunc = new Function(new ArrayList<Statement>() {
            {
                add(new VariableAssignment(testVar2, new VariableExpression(testVar)));
            }
        }, params);

        testFunc.accept(s, new Evaluator());

        assertEquals(s.getVar(testVar2), testInt);
    }

    @Test
    public void testFunctionCall() {
        final String testFuncID = "testfunc";
        final String destVar = "destvar";

        IntegerValue testInt = new IntegerValue(1);

        class TestFunc extends AbstractFunction {
            static boolean called = false;

            @Override
            public Value call(Scope s) {
                called = true;
                return testInt;
            }

            @Override
            public String getFunctionName() {
                return null;
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

            @Override
            public Value checkReturn() {
                return null;
            }
        }

        Scope s = new Scope();
        s.setVar(testFuncID, new TestFunc());

        HashMap<String, String> params = new HashMap<>();
        Function testFunc = new Function(new ArrayList<>() {
            {
                add(new VariableAssignment(destVar, new FunctionCall(testFuncID, new HashMap<>())));
            }
        }, params);


        testFunc.accept(s, new Evaluator());
        assertTrue(TestFunc.called);
        assertEquals(s.getVar(destVar).asInteger(), testInt);
    }

    @Test
    public void testUserFunctionDefCall() {
        final String userFunc = "userfunc";
        final String destVar = "destVar";
        final String testArg = "testarg";
        final String testInt = "testint";
        final Integer testIntVal = 200;


        Scope s = new Scope();
        s.setVar(testInt, new IntegerValue(testIntVal));
        HashMap<String, String> params = new HashMap<>();

        Function testFunc = new Function(new ArrayList<>() {
            {
                add(new FunctionDefinition(userFunc, new ArrayList<>() {
                    {
                        add(new Return(new VariableExpression(testArg)));
                    }
                }, params));
                add(new VariableAssignment(destVar, new FunctionCall(userFunc, Map.of(testArg, new VariableExpression(testInt)))));
            }
        }, params);
        testFunc.accept(s, new Evaluator());
        assertEquals(s.getVar(destVar).asInteger().get(), testIntVal);
    }

    @Test
    public void testLoop() {
        Scope s = new Scope();
        final String testInt = "testint";
        final String testArray = "testarray";
        final String loopVar = "i";

        Array a = new Array(new ArrayList<>(){{
            for (int i = 0; i < 10; i++) {
                add(new IntegerValue(i));
            }
        }});
        s.setVar(testInt, new IntegerValue(0));
        s.setVar(testArray, a);

        HashMap<String, String> params = new HashMap<>();
        Function testFunc = new Function(new ArrayList<>() {
            {
                add(new LoopStatement(new VariableExpression(testArray), loopVar, new ArrayList<>() {{
                    add(new VariableAssignment(
                        testInt,
                        new ArithmeticExpression(new VariableExpression(testInt), new VariableExpression(loopVar), new AdditionVisitor()))
                    );
                }}));
            }
        }, params);
        testFunc.accept(s, new Evaluator());
        assertEquals(s.getVar(testInt).asInteger().get(), 45);
    }

    @Test
    public void testIf() {
        final String intA = "inta";
        final String intB = "intb";
        final String varA = "varA";
        final String varB = "varB";

        Scope s = new Scope();
        s.setVar(intA, new IntegerValue(10));
        s.setVar(intB, new IntegerValue(20));
        s.setVar(varA, new BooleanValue(false));
        s.setVar(varB, new BooleanValue(false));

        HashMap<String, String> params = new HashMap<>();
        Function testFunc = new Function(new ArrayList<>() {
            {
                add(new IfStatement(
                    new ComparisonExpression(new VariableExpression(intA), new VariableExpression(intB), new LTVisitor()),
                    new ArrayList<>() {{
                        add (new VariableAssignment(varA, new BooleanValue(true)));
                    }}
                ));
                add(new IfStatement(
                    new ComparisonExpression(new VariableExpression(intA), new VariableExpression(intB), new GTVisitor()),
                    new ArrayList<>() {{
                        add (new VariableAssignment(varB, new BooleanValue(true)));
                    }}
                ));
            }
        }, params);
        testFunc.accept(s, new Evaluator());

        assertTrue(s.getVar(varA).asBoolean().get());
        assertFalse(s.getVar(varB).asBoolean().get());
    }
}
