package core.statements;


import core.expressions.Expression;

public final class VariableAssignment extends Statement {
    private final String dest;
    private final Expression expr;

    public VariableAssignment(String dest, Expression expr) {
        this.dest = dest;
        this.expr = expr;
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public String dest() {
        return dest;
    }

    public Expression expr() {
        return expr;
    }
}
