package ast.expressions;

import core.values.Value;

public class Literal implements ExpressionNode {
    private final Value v;

    public Literal(Value v) {
        this.v = v;
    }

    public Value getValue() {
        return this.v;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
