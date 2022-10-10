package core.expressions.comparison;

import core.Scope;
import core.expressions.Expression;
import core.values.BooleanValue;
import core.values.StringValue;

public class EQVisitor extends IntegerComparisonVisitor {
    private static final String OP = "==";

    public EQVisitor() {
        super(EQVisitor.OP);
    }

    @Override
    public BooleanValue visit(StringValue a, Expression b, Scope s) {
        return new BooleanValue(a.get().equals(b.evaluate(s).asString().get()));
    }

    @Override
    protected boolean apply(int a, int b) {
        return a == b;
    }
}
