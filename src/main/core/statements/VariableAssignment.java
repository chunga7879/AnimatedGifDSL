package core.statements;


import core.expressions.Expression;

public record VariableAssignment(String dest, Expression expr) implements Statement {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
