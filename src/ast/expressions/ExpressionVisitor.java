package ast.expressions;

public interface ExpressionVisitor<C, T> {
    T visit(C ctx, Literal literal);

    T accept(C ctx, Variable variable);

    T accept(C ctx, FunctionCallExpression fce);
}
