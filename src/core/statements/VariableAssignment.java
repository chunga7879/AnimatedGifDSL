package core.statements;


import core.Scope;
import core.expressions.Expression;

public class VariableAssignment implements Statement {
    private String dest;
    private Expression expr;

    public VariableAssignment(String dest, Expression expr) {
        this.dest = dest;
        this.expr = expr;
    }

    @Override
    public void Do(Scope s) {
        s.setVar(dest, expr.evaluate(s));
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return null;
    }
}
