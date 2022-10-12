package core.expressions;

public record VariableExpression(String identifier) implements Expression {

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
