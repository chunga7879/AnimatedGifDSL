package core.statements;

import core.Scope;
import core.expressions.Expression;
import core.values.Null;

import java.util.List;

public class IfStatement implements Statement {
    private final Expression cond;
    private final List<Statement> statements;

    public IfStatement(Expression cond, List<Statement> statements) {
        this.cond = cond;
        this.statements = statements;
    }

    @Override
    public void Do(Scope s) {
        if (!this.cond.evaluate(s).asBoolean().get()) {
            return;
        }
        // TODO do we want to support early returns?
        for (Statement stms : this.statements) {
            stms.Do(s);
        }
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return null;
    }
}
