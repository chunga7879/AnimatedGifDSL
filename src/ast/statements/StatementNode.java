package ast.statements;

public interface StatementNode {
    <C, T> T accept(C ctx, StatementVisitor<C, T> v);
}
