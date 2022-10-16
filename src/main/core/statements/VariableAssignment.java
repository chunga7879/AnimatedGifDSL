package core.statements;


import core.expressions.Expression;

public final class VariableAssignment extends Statement {
    private final String dest;
    private final Expression expr;
    private final boolean local;

    public VariableAssignment(String dest, Expression expr, boolean local) {
        this.dest = dest;
        this.expr = expr;
        this.local = local;
    }

    public VariableAssignment(String dest, Expression expr) {
        this(dest, expr, false);
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

    public boolean local() {
        return local;
    }
}
