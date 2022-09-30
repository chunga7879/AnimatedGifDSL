package core.statements;

import core.Scope;
import core.expressions.Expression;
import core.values.Value;

public class Return {
    public Expression e;

    public Return(Expression e) {
       this.e = e;
    }

    // Return the value that this return statement returns.
    public Value GetReturnValue(Scope s) {
        return this.e.evaluate(s);
    }
}
