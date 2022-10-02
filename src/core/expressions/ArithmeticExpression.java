package core.expressions;

import core.Scope;
import core.expressions.arithmetic.ArithmeticVisitor;
import core.values.Value;

public class ArithmeticExpression implements Expression {
    private final Expression a;
    private final Expression b;
    private final ArithmeticVisitor av;

    public ArithmeticExpression(Expression a, Expression b, ArithmeticVisitor av) {
        this.a = a;
        this.b = b;
        this.av = av;
    }

    @Override
    public Value evaluate(Scope s) {
        return this.a.evaluate(s).acceptAV(this.av, this.b, s);
    }
}
