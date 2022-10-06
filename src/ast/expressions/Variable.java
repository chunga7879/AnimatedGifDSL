package ast.expressions;

public class Variable implements ExpressionNode {
    private final String identifier;

    public Variable(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }

    public String getIdentifier() {
        return identifier;
    }
}
