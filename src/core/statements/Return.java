package core.statements;

import core.Scope;
import core.exceptions.InternalException;
import core.expressions.Expression;
import core.values.Value;

public class Return implements Statement {
    public Expression e;

    public Return(Expression e) {
        this.e = e;
    }

    // Return the value that this return statement returns.
    public Value GetReturnValue(Scope s) {
        return this.e.evaluate(s);
    }

    @Override
    public void Do(Scope s) {
        throw new InternalException("Return.Do() should never be called");
    }

    @Override
    public <C, T> T accept(C ctx, StatementVisitor<C, T> v) {
        return null;
    }
}
