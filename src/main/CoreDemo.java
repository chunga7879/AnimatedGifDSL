import builtin.functions.Print;
import core.Scope;
import core.evaluators.Evaluator;
import core.exceptions.DSLException;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.expressions.VariableExpression;
import core.statements.ExpressionWrapper;
import core.statements.Statement;
import core.statements.VariableAssignment;
import core.values.Function;
import core.values.StringValue;

import java.util.ArrayList;
import java.util.HashMap;

public class CoreDemo {
    public static void main(String[] args) {
        /*
         * print("hello world!");
         * myvar = "testvalue";
         * print(myvar)
         */

        Scope rootScope = new Scope();
        rootScope.setVar("print", new Print());

        ArrayList<Statement> statements = new ArrayList<>();
        HashMap<String, Expression> printArgs = new HashMap<>();
        printArgs.put("msg", new StringValue("hello world!"));
        statements.add(new ExpressionWrapper(new FunctionCall("print", printArgs, rootScope)));

        statements.add(new VariableAssignment("myvar", new StringValue("testvalue")));
        HashMap<String, Expression> printArgs2 = new HashMap<>();
        printArgs2.put("msg", new VariableExpression("myvar"));
        statements.add(new ExpressionWrapper(new FunctionCall("print", printArgs2, rootScope)));
        HashMap<String, String> params = new HashMap<>();
        Function mainFunc = new Function(statements, params);

        Evaluator evaluator = new Evaluator();
        try {
            mainFunc.accept(rootScope, evaluator);
        } catch (DSLException e) {
            System.out.println("Got dsl exception: " + e.message());
        }
    }
}
