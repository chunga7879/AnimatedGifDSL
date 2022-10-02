package core.expressions;

import core.Scope;
import core.statements.Statement;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

public class FunctionCall implements Expression, Statement {
    private final String name;
    private final HashMap<String, Expression> args;
    private final Scope scope;

    /**
     * @param name The name of the function to call.
     * @param args The arguments to pass to the function.
     */
    public FunctionCall(String name, HashMap<String, Expression> args, Scope scope) {
        this.name = name;
        this.args = args;
        this.scope = scope;
    }

    @Override
    public Value evaluate(Scope s) {
        Scope funcScope = this.scope.newChildScope();
        for (Map.Entry<String, Expression> entry : this.args.entrySet()) {
            funcScope.setVar(entry.getKey(), entry.getValue().evaluate(s));
        }
        return s.getVar(this.name).asFunction().call(funcScope);
    }

    @Override
    public void Do(Scope s) {
        this.evaluate(s);
    }
}
