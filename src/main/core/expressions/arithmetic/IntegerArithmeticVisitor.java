package core.expressions.arithmetic;

import core.Scope;
import core.expressions.Expression;
import core.values.IntegerValue;
import core.values.Value;

abstract class IntegerArithmeticVisitor extends ArithmeticVisitor {
    protected IntegerArithmeticVisitor(String op) {
        super(op);
    }

    protected abstract int apply(int a, int b);

    @Override
    public Value visit(IntegerValue a, Value b, Scope s) {
        return new IntegerValue(this.apply(a.get(), b.asInteger().get()));
    }
}
