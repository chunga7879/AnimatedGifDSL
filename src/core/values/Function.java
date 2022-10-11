package core.values;

import core.Scope;
import core.exceptions.InternalException;
import core.expressions.ExpressionVisitor;
import core.statements.Return;
import core.statements.Statement;

import java.util.List;

// A user defined function (or main).
public class Function extends AbstractFunction {
    private final List<Statement> statements;

    public Function(List<Statement> statements) {
        this.statements = statements;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public Value call(Scope scope) {
        throw new InternalException("remove me");
    }

    @Override
    public void checkArgs(Scope scope) {
        // TODO
        // Probably need to store params as field??
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
