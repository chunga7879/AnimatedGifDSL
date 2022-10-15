package core.statements;

import core.expressions.Expression;

import java.util.List;

public final class IfStatement extends Statement {
    private final Expression cond;
    private final List<Statement> statements;

    public IfStatement(Expression cond, List<Statement> statements) {
        this.cond = cond;
        this.statements = statements;
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public Expression cond() {
        return cond;
    }

    public List<Statement> statements() {
        return statements;
    }
}
