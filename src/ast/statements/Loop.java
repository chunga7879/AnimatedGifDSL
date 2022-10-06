package ast.statements;

import ast.expressions.ExpressionNode;

public record Loop(String loopVar, ExpressionNode expr) implements StatementNode {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
