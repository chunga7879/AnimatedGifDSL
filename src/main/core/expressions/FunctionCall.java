package core.expressions;

import java.util.Map;

public record FunctionCall(String identifier, Map<String, Expression> args) implements Expression {

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
