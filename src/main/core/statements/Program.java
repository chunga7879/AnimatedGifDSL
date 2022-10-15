package core.statements;

import java.util.List;

public final class Program extends Statement {
    private final List<Statement> statements;

    public Program(List<Statement> statements) {
        this.statements = statements;
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public List<Statement> statements() {
        return statements;
    }
}
