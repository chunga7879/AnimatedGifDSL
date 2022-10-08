package ast.evaluators;

import ast.expressions.ExpressionVisitor;
import ast.expressions.FunctionCallExpression;
import ast.expressions.Literal;
import ast.expressions.Variable;
import core.Scope;
import core.values.Value;

public class ExpressionEvaluator implements ExpressionVisitor<Scope, Value> {
    @Override
    public Value visit(Scope ctx, Literal literal) {
        return literal.getValue();
    }

    @Override
    public Value visit(Scope ctx, Variable variable) {
        return ctx.getVar(variable.getIdentifier());
    }

    @Override
    public Value visit(Scope ctx, FunctionCallExpression fce) {
        //return ctx.getVar(fce.func()).asFunction().evaluate(ctx);
        return null;
    }
}
