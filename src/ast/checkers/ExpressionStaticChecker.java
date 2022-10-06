package ast.checkers;

import ast.expressions.ExpressionVisitor;
import ast.expressions.FunctionCallExpression;
import ast.expressions.Literal;
import ast.expressions.Variable;
import core.Scope;
import core.values.Value;

public class ExpressionStaticChecker implements ExpressionVisitor<Scope, Value> {
    @Override
    public Value visit(Scope ctx, Literal literal) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, Variable variable) {
        return null;
    }

    @Override
    public Value visit(Scope ctx, FunctionCallExpression fce) {
        return null;
    }
}
