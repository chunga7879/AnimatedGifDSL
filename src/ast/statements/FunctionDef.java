package ast.statements;

import java.util.List;

public record FunctionDef(String identifier, List<StatementNode> statements) implements StatementNode {
    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
