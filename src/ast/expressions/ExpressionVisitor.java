package ast.expressions;

public interface ExpressionVisitor<C, T> {
    T visit(C ctx, Literal literal);

    T visit(C ctx, Variable variable);

    T visit(C ctx, FunctionCallExpression fce);
}
