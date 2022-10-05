import builtin.functions.Print;
import core.Scope;
import core.exceptions.DSLException;
import core.expressions.Expression;
import core.expressions.FunctionCall;
import core.expressions.VariableExpression;
import core.statements.Statement;
import core.statements.VariableAssignment;
import core.values.Function;
import core.values.StringValue;
import core.values.Value;

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
        statements.add(new FunctionCall("print", printArgs, rootScope));

        statements.add(new VariableAssignment("myvar", new StringValue("testvalue")));
        HashMap<String, Expression> printArgs2 = new HashMap<>();
        printArgs2.put("msg", new VariableExpression("myvar"));
        statements.add(new FunctionCall("print", printArgs2, rootScope));
        Function mainFunc = new Function(statements);
        try {
            mainFunc.call(rootScope);
        } catch (DSLException e) {
            System.out.println("Got dsl exception: " + e.message());
        }
    }
}
