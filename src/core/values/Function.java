package core.values;

import core.Scope;
import core.checkers.ArgumentChecker;
import core.exceptions.InternalException;
import core.expressions.ExpressionVisitor;
import core.statements.Statement;

import java.util.List;
import java.util.Map;

// A user defined function (or main).
public class Function extends AbstractFunction {
    private final List<Statement> statements;
    private final Map<String, String> params;

    public Function(List<Statement> statements, Map<String, String> params) {
        this.statements = statements;
        this.params = params;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    public Value call(Scope scope) {
        throw new InternalException("remove me");
    }

    @Override
    public Value checkArgs(Scope scope) {
        ArgumentChecker.check(scope, params, "user defined function");
        return checkReturn();
    }

    @Override
    public Value checkReturn() {
        return new Unknown();
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
