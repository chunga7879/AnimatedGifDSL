package core.statements;

import core.expressions.Expression;

import java.util.List;

public record LoopStatement(Expression array, String loopVar, List<Statement> statements) implements Statement {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
