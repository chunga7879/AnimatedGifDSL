package ast.expressions;

import core.expressions.Expression;

import java.util.Map;

public record FunctionCallExpression(String identifier, Map<String, Expression> params) implements ExpressionNode {
    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.accept(ctx, this);
    }
}
