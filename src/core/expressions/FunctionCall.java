package core.expressions;

import core.Scope;
import core.statements.Statement;
import core.statements.StatementVisitor;
import core.values.Value;

import java.util.HashMap;
import java.util.Map;

public class FunctionCall implements Expression, Statement {
    private final String name;
    private final HashMap<String, Expression> args;
    private final Scope scope;

    /**
     * @param name  The name of the function to call.
     * @param args  The arguments to pass to the function.
     * @param scope The scope to execute the function in (args will be added).
     */
    public FunctionCall(String name, HashMap<String, Expression> args, Scope scope) {
        this.name = name;
        this.args = args;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Expression> getArgs() {
        return args;
    }

    /**
     * @param s The scope to evaluate argument expressions in.
     * @return The return value of the function.
     */
    @Override
    public Value evaluate(Scope s) {
        Scope funcScope = this.scope.newChildScope();
        for (Map.Entry<String, Expression> entry : this.args.entrySet()) {
            funcScope.setVar(entry.getKey(), entry.getValue().evaluate(s));
        }
        return s.getVar(this.name).asFunction().call(funcScope);
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return null;
    }

    @Override
    public void Do(Scope s) {
        this.evaluate(s);
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
