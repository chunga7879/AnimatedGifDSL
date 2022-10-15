package core.statements;


import core.expressions.Expression;

public record VariableAssignment(String dest, Expression expr, boolean local) implements Statement {

    public VariableAssignment(String dest, Expression expr) {
        this(dest, expr, false);
    }
    
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
