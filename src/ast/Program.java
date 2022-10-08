package ast;

import ast.statements.StatementNode;

import java.util.List;

public record Program(List<StatementNode> statements) implements Node {
    @Override
    public <C, T> T accept(C ctx, NodeVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
