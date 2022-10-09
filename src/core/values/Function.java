package core.values;

import core.Scope;
import core.expressions.ExpressionVisitor;
import core.statements.Return;
import core.statements.Statement;

import java.util.ArrayList;
import java.util.List;

// A user defined function (or main).
public class Function extends AbstractFunction {
    private final List<Statement> statements;

    public Function(List<Statement> statements) {
        this.statements = statements;
    }

    public Value call(Scope scope) {
        for (Statement s : statements) {
            if (s instanceof Return) {
                return ((Return) s).GetReturnValue(scope);
            } else {
                s.Do(scope);
            }
        }
        return Null.NULL;
    }

    @Override
    public <C, T> T accept(C ctx, ExpressionVisitor<C, T> v) {
        return v.visit(ctx, this);
    }
}
