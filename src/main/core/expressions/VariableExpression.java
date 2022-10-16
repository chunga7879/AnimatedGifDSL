package core.expressions;

public final class VariableExpression extends Expression {
    private final String identifier;

    public VariableExpression(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public String identifier() {
        return identifier;
    }
}
