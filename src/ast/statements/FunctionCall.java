package ast.statements;

import core.expressions.Expression;

import java.util.Map;

public record FunctionCall(String identifier, Map<String, Expression> params) implements StatementNode {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
