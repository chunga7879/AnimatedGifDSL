package ast.expressions;

public interface ExpressionNode {
    <C, T> T accept(C ctx, ExpressionVisitor<C, T> v);
}
