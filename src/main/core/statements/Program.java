package core.statements;

import java.util.List;

public record Program(List<Statement> statements) implements Statement {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
