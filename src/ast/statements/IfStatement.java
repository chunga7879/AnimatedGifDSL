package ast.statements;


import ast.expressions.ExpressionNode;

import java.util.List;

public record IfStatement(ExpressionNode cond, List<StatementNode> body) implements StatementNode {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
